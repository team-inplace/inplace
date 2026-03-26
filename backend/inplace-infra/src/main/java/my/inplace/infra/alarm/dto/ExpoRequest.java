package my.inplace.infra.alarm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExpoRequest(
    String to,
    String title,
    String body,
    Object data
) {

    public static ExpoRequest ofComment(
        String token,
        String title,
        String body,
        Long alarmId,
        Long postId,
        Long commentId
    ) {
        return new ExpoRequest(
            token,
            title,
            body,
            new CommentData(alarmId, postId, commentId)
        );
    }

    public static ExpoRequest ofReport(
        String token,
        String title,
        String body,
        Long alarmId
    ) {
        return new ExpoRequest(
            token,
            title,
            body,
            new ReportData(alarmId)
        );
    }

    public record CommentData(
        Long alarmId,
        Long postId,
        Long commentId
    ) {
    }

    public record ReportData(
        Long alarmId
    ) {
    }
}
