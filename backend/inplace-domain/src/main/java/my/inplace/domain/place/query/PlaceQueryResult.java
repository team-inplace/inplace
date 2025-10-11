package my.inplace.domain.place.query;

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
        Long likeCount,
        Boolean isLiked
    ) {

    }

    public record SimplePlace(
        Long placeId,
        String placeName,
        String address1,
        String address2,
        String address3
    ) {

    }

    public record Marker(
        Long placeId,
        String parentsCategory,
        Double longitude,
        Double latitude
    ) {

    }

    public record MarkerDetail(
        Long placeId,
        String placeName,
        String category,
        String address1,
        String address2,
        String address3
    ) {

    }
}
