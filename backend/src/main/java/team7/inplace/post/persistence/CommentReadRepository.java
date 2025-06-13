package team7.inplace.post.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team7.inplace.post.persistence.dto.CommentQueryResult;

public interface CommentReadRepository {

    Page<CommentQueryResult.DetailedComment> findCommentsByPostId(
        Long postId,
        Long userId,
        Pageable pageable
    );
}
