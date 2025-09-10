package my.inplace.application.post.query.dto;

import java.time.LocalDateTime;
import my.inplace.domain.post.query.CommentQueryResult;

public class CommentResult {

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

        public static DetailedComment from(CommentQueryResult.DetailedComment comment) {
            return new DetailedComment(
                comment.commentId(),
                comment.userNickname(),
                comment.userImageUrl(),
                comment.tierImageUrl(),
                comment.mainBadgeImageUrl(),
                comment.content(),
                comment.selfLike(),
                comment.totalLikeCount(),
                comment.isMine(),
                comment.createdAt()
            );
        }

    }
}
