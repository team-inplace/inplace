package team7.inplace.place.application;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;
import team7.inplace.global.exception.code.PlaceErrorCode;
import team7.inplace.kakao.application.command.PlaceMessageCommand;
import team7.inplace.liked.likedPlace.domain.LikedPlace;
import team7.inplace.liked.likedPlace.persistence.LikedPlaceRepository;
import team7.inplace.place.application.command.PlaceLikeCommand;
import team7.inplace.place.application.command.PlacesCommand.Coordinate;
import team7.inplace.place.application.command.PlacesCommand.Create;
import team7.inplace.place.application.command.PlacesCommand.FilterParams;
import team7.inplace.place.application.dto.LikedPlaceInfo;
import team7.inplace.place.application.dto.PlaceQueryInfo;
import team7.inplace.place.domain.Category;
import team7.inplace.place.persistence.PlaceReadRepository;
import team7.inplace.place.persistence.PlaceSaveRepository;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.review.persistence.ReviewJPARepository;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.video.persistence.VideoReadRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceReadRepository placeReadRepository;
    private final PlaceSaveRepository placeSaveRepository;

    private final ReviewJPARepository reviewJPARepository;
    private final LikedPlaceRepository likedPlaceRepository;
    private final VideoReadRepository videoReadRepository;

    public Page<PlaceQueryInfo.Simple> getPlacesInMapRange(
        Coordinate coordinateCommand,
        FilterParams filterParamsCommand,
        Pageable pageable
    ) {
        var categoryFilters = filterParamsCommand.getCategoryFilters();
        var influencerFilters = filterParamsCommand.getInfluencerFilters();

        // 위치와 필터링으로 Place 조회
        var userId = AuthorizationUtil.getUserId();
        var placesPage = getPlacesByDistance(
            coordinateCommand,
            categoryFilters,
            influencerFilters,
            pageable,
            userId
        );
        if (placesPage.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        // Place 에 대한 Video 조회
        var placeIds = placesPage.getContent().stream()
            .map(PlaceQueryResult.DetailedPlace::placeId)
            .toList();
        var placeVideos = videoReadRepository.findSimpleVideosByPlaceIds(placeIds);

        List<PlaceQueryInfo.Simple> placeInfos = placesPage.getContent().stream()
            .map(place -> PlaceQueryInfo.Simple.from(place, placeVideos.get(place.placeId())))
            .toList();
        return new PageImpl<>(placeInfos, pageable, placesPage.getTotalElements());
    }

    private Page<PlaceQueryResult.DetailedPlace> getPlacesByDistance(
        Coordinate placesCoordinateCommand,
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
            categoryFilters,
            influencerFilters,
            pageable,
            userId
        );
    }

    @Transactional(readOnly = true)
    public PlaceQueryInfo.Detail getPlaceDetailInfo(Long placeId) {
        var userId = AuthorizationUtil.getUserId();

        var detailedPlace = placeReadRepository.findDetailedPlaceById(placeId, userId)
            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));
        var placeVideos = videoReadRepository.findSimpleVideosByPlaceId(placeId);
        var placeReviewRate = reviewJPARepository.countRateByPlaceId(placeId);

        return PlaceQueryInfo.Detail.from(detailedPlace, placeVideos, placeReviewRate);
    }

    @Transactional
    public void updateLikedPlace(PlaceLikeCommand command) {
        Long userId = AuthorizationUtil.getUserId();
        if (Objects.isNull(userId)) {
            throw InplaceException.of(AuthorizationErrorCode.NOT_AUTHENTICATION);
        }

        Long placeId = command.placeId();
        LikedPlace likedPlace = likedPlaceRepository.findByUserIdAndPlaceId(userId, placeId)
            .orElseGet(() -> LikedPlace.from(userId, placeId));

        likedPlace.updateLike(command.likes());
        likedPlaceRepository.save(likedPlace);
    }

    public Long createPlace(Create placeCommand) {
        var placeBulk = placeCommand.toEntity();
        return placeSaveRepository.save(placeBulk);
    }

    //TODO: 한 장소에 비디오가 여러개일 수 있으니 수정 필요
    public PlaceMessageCommand getPlaceMessageCommand(Long placeId) {
        var place = placeReadRepository.findSimplePlaceById(placeId)
            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));
        var video = videoReadRepository.findSimpleVideosByPlaceId(placeId);

        return PlaceMessageCommand.from(place, video.get(0));
    }

    public Page<LikedPlaceInfo> getLikedPlaceInfo(Long userId, Pageable pageable) {
        var likedPlaceQueryResult = placeReadRepository.findLikedPlacesByUserIdWithPaging(userId,
            pageable);

        var likedPlaceInfos = likedPlaceQueryResult.getContent().stream()
            .map(LikedPlaceInfo::of)
            .toList();
        return new PageImpl<>(likedPlaceInfos, pageable, likedPlaceQueryResult.getTotalElements());
    }

    public List<PlaceQueryResult.Location> getPlaceLocations(
        Coordinate coordinateCommand,
        FilterParams filterParamsCommand
    ) {
        List<Category> categoryFilter = filterParamsCommand.getCategoryFilters();
        List<String> influencerFilter = filterParamsCommand.getInfluencerFilters();

        return placeReadRepository.findPlaceLocationsInMapRange(
            coordinateCommand.topLeftLongitude(),
            coordinateCommand.topLeftLatitude(),
            coordinateCommand.bottomRightLongitude(),
            coordinateCommand.bottomRightLatitude(),
            categoryFilter,
            influencerFilter
        );
    }

    public PlaceQueryResult.Marker getMarkerInfo(Long placeId) {
        return placeReadRepository.findPlaceMarkerById(placeId);
    }
}
