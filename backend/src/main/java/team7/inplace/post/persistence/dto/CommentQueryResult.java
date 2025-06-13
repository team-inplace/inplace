package team7.inplace.post.persistence.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public class CommentQueryResult {

    public record DetailedComment(
        Long commentId,
        String userNickname,
        String userImageUrl,
        String content,
        Boolean selfLike,
        Integer totalLikeCount,
        Boolean isMine,
        LocalDateTime createdAt
    ) {

        @QueryProjection
        public DetailedComment {
        }
    }

}