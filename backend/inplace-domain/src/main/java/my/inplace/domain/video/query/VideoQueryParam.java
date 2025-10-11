package my.inplace.domain.video.query;

public class VideoQueryParam {
    public record Condition(
        Boolean placeRegistration,
        Long influencerId
    ) {

        public static Condition of(Boolean placeRegistration, Long influencerId) {
            return new Condition(placeRegistration, influencerId);
        }
    }

    public record SquareBound(
        Double topLeftLongitude,
        Double topLeftLatitude,
        Double bottomRightLongitude,
        Double bottomRightLatitude,
        Double longitude,
        Double latitude
    ) {

    }
}
