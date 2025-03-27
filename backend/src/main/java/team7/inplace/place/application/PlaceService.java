package team7.inplace.place.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;
import team7.inplace.global.exception.code.PlaceErrorCode;
import team7.inplace.liked.likedPlace.domain.LikedPlace;
import team7.inplace.liked.likedPlace.persistence.LikedPlaceRepository;
import team7.inplace.place.application.command.PlaceLikeCommand;
import team7.inplace.place.application.command.PlacesCommand.Coordinate;
import team7.inplace.place.application.command.PlacesCommand.Create;
import team7.inplace.place.application.command.PlacesCommand.FilterParams;
import team7.inplace.place.application.command.PlacesCommand.RegionFilter;
import team7.inplace.place.application.dto.PlaceInfo;
import team7.inplace.place.client.GooglePlaceClient;
import team7.inplace.place.client.GooglePlaceClientResponse;
import team7.inplace.place.domain.Category;
import team7.inplace.place.persistence.PlaceJpaRepository;
import team7.inplace.place.persistence.PlaceReadRepository;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.video.persistence.VideoReadRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    private final LikedPlaceRepository likedPlaceRepository;
    private final PlaceReadRepository placeReadRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final GooglePlaceClient googlePlaceClient;
    private final VideoReadRepository videoReadRepository;

    public Long createPlace(Create placeCommand) {
        var existPlace = placeJpaRepository.findPlaceByKakaoPlaceId(placeCommand.kakaoPlaceId());
        if (existPlace.isPresent()) {
            return existPlace.get().getId();
        }
        var placeBulk = placeCommand.toEntity();
        placeJpaRepository.save(placeBulk);
        return placeBulk.getId();
    }

    @Transactional
    public void updateLikedPlace(Long userId, PlaceLikeCommand command) {
        if (Objects.isNull(userId)) {
            throw InplaceException.of(AuthorizationErrorCode.NOT_AUTHENTICATION);
        }

        Long placeId = command.placeId();
        LikedPlace likedPlace = likedPlaceRepository.findByUserIdAndPlaceId(userId, placeId)
            .orElseGet(() -> LikedPlace.from(userId, placeId));

        likedPlace.updateLike(command.likes());
        likedPlaceRepository.save(likedPlace);
    }

    @Transactional(readOnly = true)
    public Page<PlaceQueryResult.DetailedPlace> getPlacesInMapRange(
        Long userId,
        Coordinate coordinateCommand,
        FilterParams filterParamsCommand,
        Pageable pageable
    ) {
        var regionFilters = filterParamsCommand.getRegionFilters();
        var categoryFilters = filterParamsCommand.getCategoryFilters();
        var influencerFilters = filterParamsCommand.getInfluencerFilters();

        // 위치와 필터링으로 Place 조회
        var placesPage = getPlacesByDistance(
            coordinateCommand, regionFilters, categoryFilters, influencerFilters,
            pageable, userId);
        if (placesPage.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        return placesPage;
    }

    private Page<PlaceQueryResult.DetailedPlace> getPlacesByDistance(
        Coordinate placesCoordinateCommand,
        List<RegionFilter> regionFilters,
        List<Category> categoryFilters,
        List<String> influencerFilters,
        Pageable pageable,
        Long userId
    ) {
        return placeReadRepository.findPlacesInMapRangeWithPaging(
            placesCoordinateCommand.topLeftLongitude(),
            placesCoordinateCommand.topLeftLatitude(),
            placesCoordinateCommand.bottomRightLongitude(),
            placesCoordinateCommand.bottomRightLatitude(),
            placesCoordinateCommand.longitude(),
            placesCoordinateCommand.latitude(),
            regionFilters,
            categoryFilters,
            influencerFilters,
            pageable,
            userId
        );
    }

    //TODO: 한 장소에 비디오가 여러개일 수 있으니 수정 필요
    @Transactional(readOnly = true)
    public PlaceInfo.Simple getPlaceMessageCommand(Long placeId, Long userId) {
        var place = placeReadRepository.findDetailedPlaceById(placeId, userId)
            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));
        var videos = videoReadRepository.findSimpleVideosByPlaceId(placeId);

        return PlaceInfo.Simple.of(place, videos);
    }

    @Transactional(readOnly = true)
    public Page<PlaceQueryResult.DetailedPlace> getLikedPlaceInfo(Long userId, Pageable pageable) {

        return placeReadRepository.findLikedPlacesByUserIdWithPaging(userId, pageable);
    }

    @Transactional(readOnly = true)
    public List<PlaceQueryResult.Location> getPlaceLocations(
        Coordinate coordinateCommand,
        FilterParams filterParamsCommand
    ) {
        var regionFilters = filterParamsCommand.getRegionFilters();
        var categoryFilter = filterParamsCommand.getCategoryFilters();
        var influencerFilter = filterParamsCommand.getInfluencerFilters();

        return placeReadRepository.findPlaceLocationsInMapRange(
            coordinateCommand.topLeftLongitude(),
            coordinateCommand.topLeftLatitude(),
            coordinateCommand.bottomRightLongitude(),
            coordinateCommand.bottomRightLatitude(),
            regionFilters,
            categoryFilter,
            influencerFilter
        );
    }

    @Transactional(readOnly = true)
    public PlaceQueryResult.Marker getMarkerInfo(Long placeId) {
        return placeReadRepository.findPlaceMarkerById(placeId);
    }

    @Transactional(readOnly = true)
    public PlaceQueryResult.DetailedPlace getPlaceInfo(Long userId, Long placeId) {
        return placeReadRepository.findDetailedPlaceById(userId, placeId)
            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Mono<GooglePlaceClientResponse.Place> getGooglePlaceInfo(String googlePlaceId) {
        return googlePlaceClient.requestForPlaceDetail(googlePlaceId);
    }

    public List<PlaceInfo.Category> getCategories() {
        return Arrays.stream(Category.values())
            .map(category -> new PlaceInfo.Category(category.name()))
            .toList();
    }
}
