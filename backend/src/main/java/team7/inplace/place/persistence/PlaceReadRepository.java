package team7.inplace.place.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team7.inplace.place.domain.Category;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.place.persistence.dto.PlaceQueryResult.DetailedPlace;

public interface PlaceReadRepository {
    Optional<PlaceQueryResult.DetailedPlaceBulk> findDetailedPlaceById(Long placeId, Long userId);

    Optional<PlaceQueryResult.SimplePlace> findSimplePlaceById(Long placeId);

    Page<DetailedPlace> findPlacesInMapRangeWithPaging(
            Double topLeftLongitude,
            Double topLeftLatitude,
            Double bottomRightLongitude,
            Double bottomRightLatitude,
            Double longitude,
            Double latitude,
            List<Category> categoryFilters,
            List<String> influencerFilters,
            Pageable pageable,
            Long userId
    );

    List<PlaceQueryResult.Location> findPlaceLocationsInMapRange(
            Double topLeftLongitude,
            Double topLeftLatitude,
            Double bottomRightLongitude,
            Double bottomRightLatitude,
            List<Category> categoryFilters,
            List<String> influencerFilters
    );

    Page<PlaceQueryResult.DetailedPlaceBulk> findLikedPlacesByUserIdWithPaging(Long userId, Pageable pageable);

    PlaceQueryResult.Marker findPlaceMarkerById(Long placeId);
}
