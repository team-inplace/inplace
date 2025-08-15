package video.query.dto;

public record VideoParam(
    String topLeftLongitude,
    String topLeftLatitude,
    String bottomRightLongitude,
    String bottomRightLatitude,
    String longitude,
    String latitude
) {

    private VideoParam(String longitude, String latitude) {
        this(
            calculateTopLeftLongitude(longitude),   // topLeftLongitude
            calculateTopLeftLatitude(latitude),     // topLeftLatitude
            calculateBottomRightLongitude(longitude), // bottomRightLongitude
            calculateBottomRightLatitude(latitude),   // bottomRightLatitude
            longitude,
            latitude
        );
    }

    public static VideoParam from(String longitude, String latitude) {
        return new VideoParam(longitude, latitude);
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
