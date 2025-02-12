package team7.inplace.place.persistence.dto;

import com.querydsl.core.annotations.QueryProjection;

public class PlaceQueryResult {

    public record DetailedPlace(
        Long placeId,
        String placeName,
        String address1,
        String address2,
        String address3,
        Double longitude,
        Double latitude,
        String category,
        String googlePlaceId,
        Long kakaoPlaceId,
        Boolean isLiked
    ) {

        @QueryProjection
        public DetailedPlace {
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
        String category,
        String address1,
        String address2,
        String address3
    ) {

        @QueryProjection
        public Marker {
        }
    }
}
