package my.inplace.domain.place.query;

import java.util.List;
import java.util.Optional;
import my.inplace.common.cursor.CursorResult;
import my.inplace.domain.place.query.PlaceQueryResult.DetailedPlace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlaceReadRepository {

    Optional<PlaceQueryResult.DetailedPlace> findDetailedPlaceById(Long userId, Long placeId);

    Optional<PlaceQueryResult.SimplePlace> findSimplePlaceById(Long placeId);

    CursorResult<DetailedPlace, Long> findPlacesInMapRangeOrderBy(
        PlaceQueryParam.Coordinate coordinateParams,
        PlaceQueryParam.Filter filterParams,
        Long userId,
        int size,
        String orderBy,
        Long cursorValue,
        Long cursorId
    );

    List<PlaceQueryResult.Marker> findPlaceLocationsInMapRange(
        PlaceQueryParam.Coordinate coordinateParams,
        PlaceQueryParam.Filter filterParams
    );

    Page<PlaceQueryResult.DetailedPlace> findLikedPlacesByUserIdWithPaging(
        Long userId,
        Pageable pageable
    );

    PlaceQueryResult.MarkerDetail findPlaceMarkerById(Long placeId);

    List<PlaceQueryResult.Marker> findPlaceLocationsByName(
        String name,
        PlaceQueryParam.Filter filterParams
    );

    CursorResult<PlaceQueryResult.DetailedPlace, Double> findPlacesByNameOrderBy(
        Long userId,
        String name,
        PlaceQueryParam.Filter filterParams,
        int size,
        Double cursorValue,
        Long cursorId,
        String orderBy
    );

    Page<PlaceQueryResult.DetailedPlace> findPlacesByNameWithPaging(
        Long userId,
        String name,
        PlaceQueryParam.Filter filterParams,
        Pageable pageable
    );

    List<PlaceQueryResult.DetailedPlace> getDetailedPlacesByVideoId(
        Long videoId
    );
}
