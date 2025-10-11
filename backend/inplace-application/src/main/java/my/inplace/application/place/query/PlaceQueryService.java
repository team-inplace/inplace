package my.inplace.application.place.query;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.CategoryErrorCode;
import my.inplace.common.exception.code.PlaceErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.place.query.PlaceQueryResult;
import my.inplace.domain.place.query.PlaceReadRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.domain.place.client.GooglePlaceClient;
import my.inplace.domain.place.client.GooglePlaceClientResponse;
import my.inplace.infra.place.jpa.CategoryJpaRepository;
import my.inplace.infra.place.jpa.PlaceJpaRepository;
import my.inplace.application.place.query.dto.PlaceParam;
import my.inplace.application.place.query.dto.PlaceResult;
import my.inplace.application.place.query.dto.PlaceResult.Category;
import my.inplace.domain.video.query.VideoReadRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceQueryService {

    private final PlaceReadRepository placeReadRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final GooglePlaceClient googlePlaceClient;
    private final CategoryJpaRepository categoryRepository;
    private final VideoReadRepository videoReadRepository;

    public PlaceQueryResult.DetailedPlace getPlaceInfo(final Long userId, final Long placeId) {
        return placeReadRepository.findDetailedPlaceById(userId, placeId)
            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));
    }

    public List<PlaceQueryResult.DetailedPlace> getSimplePlacesByVideoId(Long videoId) {
        return placeReadRepository.getDetailedPlacesByVideoId(videoId);
    }

    public Page<PlaceQueryResult.DetailedPlace> getPlacesInMapRange(
        Long userId,
        PlaceParam.Coordinate coordinateParam,
        PlaceParam.Filter filterParam,
        Pageable pageable
    ) {

        var coordinateQueryParam = coordinateParam.toQueryParam();
        var filterQueryParam = filterParam.toQueryParam();

        var placeQueryResult = placeReadRepository.findPlacesInMapRangeWithPaging(
            coordinateQueryParam,
            filterQueryParam,
            pageable,
            userId
        );

        if (placeQueryResult.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        return placeQueryResult;
    }

    public Page<PlaceQueryResult.DetailedPlace> getPlacesByName(
        Long userId, String name, PlaceParam.Filter filterParam, Pageable pageable
    ) {
        var filterQueryParam = filterParam.toQueryParam();

        return placeReadRepository.findPlacesByNameWithPaging(
            userId,
            name,
            filterQueryParam,
            pageable
        );
    }

    public Optional<String> getGooglePlaceId(Long placeId) {
        return placeJpaRepository.findGooglePlaceIdById(placeId);
    }

    public GooglePlaceClientResponse.Place getGooglePlaceInfo(String googlePlaceId) {
        return googlePlaceClient.requestForPlaceDetail(googlePlaceId);
    }

    public List<PlaceResult.Category> getCategories() {
        var categories = categoryRepository.findAll();
        return categories.stream()
            .map(Category::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<Long> getParentCategoryIds() {
        return categoryRepository.findParentCategoryIds();
    }

    /*
     *   TODO: 한 장소에 비디오가 여러개일 수 있으니 수정 필요 / Command를 Facade에서 만들도록 변경
     */
    @Transactional(readOnly = true)
    public PlaceResult.Simple getPlaceMessageCommand(Long placeId, Long userId) {
        var place = placeReadRepository.findDetailedPlaceById(placeId, userId)
            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));
        var videos = videoReadRepository.findSimpleVideosByPlaceId(placeId);

        return PlaceResult.Simple.of(place, videos);
    }

    @Transactional(readOnly = true)
    public Page<PlaceQueryResult.DetailedPlace> getLikedPlaceInfo(Long userId, Pageable pageable) {

        return placeReadRepository.findLikedPlacesByUserIdWithPaging(userId, pageable);
    }

    /*
     * Marker 관련 조회 매서드
     */
    public PlaceQueryResult.MarkerDetail getMarkerInfo(Long placeId) {
        return placeReadRepository.findPlaceMarkerById(placeId);
    }

    public List<PlaceQueryResult.Marker> getPlaceLocations(
        PlaceParam.Coordinate coordinatePrams,
        PlaceParam.Filter filterParams
    ) {
        var coordinateQueryParam = coordinatePrams.toQueryParam();
        var filterQueryParam = filterParams.toQueryParam();

        return placeReadRepository.findPlaceLocationsInMapRange(
            coordinateQueryParam,
            filterQueryParam
        );
    }

    public List<PlaceQueryResult.Marker> getPlaceLocationsByName(
        String name,
        PlaceParam.Filter filterParams
    ) {
        var filterQueryParam = filterParams.toQueryParam();
        return placeReadRepository.findPlaceLocationsByName(
            name,
            filterQueryParam
        );
    }


    @Transactional(readOnly = true)
    public List<PlaceResult.Category> getSubCategoriesByParentId(Long parentId) {
        return categoryRepository.findSubCategoriesByParentId(parentId)
            .stream().map(PlaceResult.Category::from).toList();
    }

    public List<PlaceResult.Category> getSubCategories() {
        return categoryRepository.findSubCategories()
            .stream().map(PlaceResult.Category::from).toList();
    }

    public List<PlaceResult.Category> getParentCategories() {
        return categoryRepository.findParentCategories()
            .stream().map(PlaceResult.Category::from).toList();
    }

    public PlaceResult.Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .map(PlaceResult.Category::from)
            .orElseThrow(() -> InplaceException.of(CategoryErrorCode.NOT_FOUND));
    }
}
