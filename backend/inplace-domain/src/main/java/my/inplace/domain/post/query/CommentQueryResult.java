package my.inplace.domain.post.query;

import java.time.LocalDateTime;

public class CommentQueryResult {

    public record DetailedComment(
        Long commentId,
        String userNickname,
        String userImageUrl,
        String tierImageUrl,
        String mainBadgeImageUrl,
        String content,
        Boolean selfLike,
        Integer totalLikeCount,
        Boolean isMine,
        LocalDateTime createdAt
    ) {

    }

}
