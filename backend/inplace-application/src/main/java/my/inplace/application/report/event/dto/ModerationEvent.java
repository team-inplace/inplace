package my.inplace.application.report.event.dto;

public class ModerationEvent {

    public record PostDeleted(
        Long postId,
        Long receiverId,
        String postTitle
    ) {

    }

    public record CommentDeleted(
        Long postId,
        Long commentId,
        Long receiverId,
        String postTitle
    ) {

    }

}