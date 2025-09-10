package my.inplace.domain.place.query;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlaceReadRepository {

    Optional<PlaceQueryResult.DetailedPlace> findDetailedPlaceById(Long userId, Long placeId);

    Optional<PlaceQueryResult.SimplePlace> findSimplePlaceById(Long placeId);

    Page<PlaceQueryResult.DetailedPlace> findPlacesInMapRangeWithPaging(
        PlaceQueryParam.Coordinate coordinateParams,
        PlaceQueryParam.Filter filterParams,
        Pageable pageable,
        Long userId
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
