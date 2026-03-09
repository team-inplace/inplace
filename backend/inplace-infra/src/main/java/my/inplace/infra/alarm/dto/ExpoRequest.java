package my.inplace.infra.alarm.dto;

public record ExpoRequest(
    String to,
    String title,
    String body,
    AlarmData.Comment data
) {
    public static ExpoRequest of(String to, String title, String body, AlarmData.Comment data) {
        return new ExpoRequest(to, title, body, data);
    }
}
