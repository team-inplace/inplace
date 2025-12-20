package my.inplace.infra.alarm.dto;

public record ExpoRequest(
    String to,
    String title,
    String body
) {
    public static ExpoRequest of(String to, String title, String body) {
        return new ExpoRequest(to, title, body);
    }
}
