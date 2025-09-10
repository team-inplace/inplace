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

}
