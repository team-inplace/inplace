package team7.inplace.place.persistence.dto;

import com.querydsl.core.annotations.QueryProjection;
import team7.inplace.place.domain.Category;

public class PlaceQueryResult {

    public record DetailedPlace(
        Long placeId,
        String placeName,
        String address1,
        String address2,
        String address3,
        Double longitude,
        Double latitude,
        Category category,
        String googlePlaceId,
        Long kakaoPlaceId,
        Boolean isLiked
    ) {

        @QueryProjection
        public DetailedPlace {
        }

        public boolean haveNoGooglePlaceId() {
            return googlePlaceId == null;
        }
    }

    public record SimplePlace(
        Long placeId,
        String placeName,
        String address1,
        String address2,
        String address3
    ) {

        @QueryProjection
        public SimplePlace {
        }
    }

    public record Location(
        Long placeId,
        Double longitude,
        Double latitude
    ) {

        @QueryProjection
        public Location {
        }
    }

    public record Marker(
        Long placeId,
        String placeName,
        Category category,
        String address1,
        String address2,
        String address3
    ) {

        @QueryProjection
        public Marker {
        }
    }
}