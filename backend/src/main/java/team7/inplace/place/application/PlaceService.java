package team7.inplace.place.application;

import exception.InplaceException;
import exception.code.AuthorizationErrorCode;
import exception.code.CategoryErrorCode;
import exception.code.PlaceErrorCode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import place.LikedPlace;
import place.Place;
import place.PlaceVideo;
import place.client.GooglePlaceClientResponse;
import place.query.PlaceQueryResult;
import place.query.PlaceQueryResult.Marker;
import place.query.PlaceQueryResult.MarkerDetail;
import place.query.PlaceReadRepository;
import team7.inplace.admin.dto.CategoryForm;
import team7.inplace.place.application.command.PlaceLikeCommand;
import team7.inplace.place.application.command.PlacesCommand;
import team7.inplace.place.application.command.PlacesCommand.Coordinate;
import team7.inplace.place.application.command.PlacesCommand.FilterParams;
import team7.inplace.place.application.command.PlacesCommand.RegionParam;
import team7.inplace.place.application.command.PlacesCommand.Upsert;
import team7.inplace.place.application.dto.PlaceInfo;
import team7.inplace.place.application.dto.PlaceInfo.Category;
import team7.inplace.place.client.RestTemplateGooglePlaceClient;
import team7.inplace.place.persistence.CategoryRepository;
import team7.inplace.place.persistence.LikedPlaceRepository;
import team7.inplace.place.persistence.PlaceJpaRepository;
import team7.inplace.place.persistence.PlaceVideoJpaRepository;
import video.query.VideoReadRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceReadRepository placeReadRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final CategoryRepository categoryRepository;
    private final PlaceVideoJpaRepository placeVideoJpaRepository;
    private final LikedPlaceRepository likedPlaceRepository;
    private final VideoReadRepository videoReadRepository;
    private final RestTemplateGooglePlaceClient restTemplateGooglePlaceClient;

    @Transactional
    public void createPlace(Upsert placeCommand) {
        var place = placeJpaRepository.findPlaceByKakaoPlaceId(placeCommand.kakaoPlaceId())
            .orElseGet(() -> {
                var newPlace = placeCommand.toEntity();
                return placeJpaRepository.save(newPlace);
            });

        var placeVideo = new PlaceVideo(place.getId(), placeCommand.videoId());
        placeVideoJpaRepository.save(placeVideo);
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
        var regionFilters = filterParamsCommand.regions();
        var categoryFilters = filterParamsCommand.categories();
        var influencerFilters = filterParamsCommand.influencers();

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
        List<RegionParam> regionParams,
        List<Long> categoryFilters,
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
            regionParams,
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
    public List<Marker> getPlaceLocations(
        Coordinate coordinateCommand,
        FilterParams filterParamsCommand
    ) {
        var regionFilters = filterParamsCommand.regions();
        var categoryFilter = filterParamsCommand.categories();
        var influencerFilter = filterParamsCommand.influencers();

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
    public MarkerDetail getMarkerInfo(Long placeId) {
        return placeReadRepository.findPlaceMarkerById(placeId);
    }

    @Transactional(readOnly = true)
    public Optional<String> getGooglePlaceId(Long placeId) {
        return placeJpaRepository.findGooglePlaceIdById(placeId);
    }

    @Transactional(readOnly = true)
    public PlaceQueryResult.DetailedPlace getPlaceInfo(Long userId, Long placeId) {
        return placeReadRepository.findDetailedPlaceById(userId, placeId)
            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));
    }

    public GooglePlaceClientResponse.Place getGooglePlaceInfo(String googlePlaceId) {
        return restTemplateGooglePlaceClient.requestForPlaceDetail(googlePlaceId);
    }

    public List<PlaceInfo.Category> getCategories() {
        var categories = categoryRepository.findAll();
        return categories.stream()
            .map(Category::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<Long> getParentCategoryIds() {
        return categoryRepository.findParentCategoryIds();
    }

    @Transactional(readOnly = true)
    public List<PlaceInfo.Category> getSubCategoriesByParentId(Long parentId) {
        return categoryRepository.findSubCategoriesByParentId(parentId)
            .stream().map(PlaceInfo.Category::from).toList();
    }

    public List<Marker> getPlaceLocationsByName(String name, FilterParams command) {
        return placeReadRepository.findPlaceLocationsByName(
            name,
            command.regions(),
            command.categories(),
            command.influencers()
        );
    }

    public Page<PlaceQueryResult.DetailedPlace> getPlacesByName(
        Long userId, String name, PlacesCommand.FilterParams command, Pageable pageable
    ) {
        return placeReadRepository.findPlacesByNameWithPaging(
            userId,
            name,
            command.regions(),
            command.categories(),
            command.influencers(),
            pageable
        );
    }

    public List<PlaceQueryResult.DetailedPlace> getSimplePlacesByVideoId(Long videoId) {
        return placeReadRepository.getDetailedPlacesByVideoId(videoId);
    }

    @Transactional
    public void deletePlaceById(Long placeId) {
        placeJpaRepository.deleteById(placeId);
    }

    @Transactional
    public Long updatePlaceInfo(Long placeId, Upsert command) {
        Place place = placeJpaRepository.findById(placeId)
            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));

        // TODO : 종속성 해결
//        place.updateInfo(command);

        return place.getId();
    }

    @Transactional
    public void updateCategory(Long categoryId, CategoryForm categoryForm) {
        place.Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> InplaceException.of(CategoryErrorCode.NOT_FOUND));

        category.updateInfo(
            categoryForm.getName(),
            categoryForm.getEngName(),
            categoryForm.getParentId()
        );
    }

    public void deleteCategoryById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
