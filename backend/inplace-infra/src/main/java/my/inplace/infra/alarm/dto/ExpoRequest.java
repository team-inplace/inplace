package my.inplace.infra.alarm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExpoRequest(
    String to,
    String title,
    String body,
    Object data
) {
    public static ExpoRequest of(String token, String title, String body) {
        return new ExpoRequest(token, title, body, null);
    }

    public static ExpoRequest ofComment(
        String token,
        String title,
        String body,
        Long postId,
        Long commentId
    ) {
        return new ExpoRequest(
            token,
            title,
            body,
            new CommentData(postId, commentId)
        );
    }

    public record CommentData(
        Long postId,
        Long commentId
    ) {
    }
}
