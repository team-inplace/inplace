package my.inplace.application.video.query.dto;

import my.inplace.domain.video.query.VideoQueryParam;

public class VideoParam {

    public record SquareBound(
        String topLeftLongitude,
        String topLeftLatitude,
        String bottomRightLongitude,
        String bottomRightLatitude,
        String longitude,
        String latitude
    ) {

        private SquareBound(String longitude, String latitude) {
            this(
                calculateTopLeftLongitude(longitude),   // topLeftLongitude
                calculateTopLeftLatitude(latitude),     // topLeftLatitude
                calculateBottomRightLongitude(longitude), // bottomRightLongitude
                calculateBottomRightLatitude(latitude),   // bottomRightLatitude
                longitude,
                latitude
            );
        }

        public static SquareBound from(String longitude, String latitude) {
            return new SquareBound(longitude, latitude);
        }

        private static String calculateTopLeftLongitude(String longitude) {
            return String.valueOf(Double.parseDouble(longitude) - 0.03);
        }

        private static String calculateTopLeftLatitude(String latitude) {
            return String.valueOf(Double.parseDouble(latitude) + 0.03);
        }

        private static String calculateBottomRightLongitude(String longitude) {
            return String.valueOf(Double.parseDouble(longitude) + 0.03);
        }

        private static String calculateBottomRightLatitude(String latitude) {
            return String.valueOf(Double.parseDouble(latitude) - 0.03);
        }
    }

    public record Condition(
        Boolean placeRegistration,
        Long influencerId
    ) {

        public static Condition of(Boolean placeRegistration, Long influencerId) {
            return new Condition(placeRegistration, influencerId);
        }

        public VideoQueryParam.Condition toQueryParam() {
            return new VideoQueryParam.Condition(
                placeRegistration,
                influencerId
            );
        }
    }
}
