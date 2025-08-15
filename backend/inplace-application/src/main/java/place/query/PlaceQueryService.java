package place.query;

import exception.InplaceException;
import exception.code.PlaceErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import place.dto.PlaceInfo;
import place.jpa.CategoryJpaRepository;
import place.jpa.PlaceJpaRepository;
import place.query.PlaceQueryResult.Marker;
import place.query.dto.PlaceParam;
import place.query.dto.PlaceResult;
import video.query.VideoReadRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceQueryService {

    private final PlaceReadRepository placeReadRepository;
    private final PlaceJpaRepository placeJpaRepository;
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

    /*
     *   TODO: 한 장소에 비디오가 여러개일 수 있으니 수정 필요 / Command를 Facade에서 만들도록 변경
     */
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
}
