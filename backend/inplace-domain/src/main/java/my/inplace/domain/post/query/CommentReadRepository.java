package my.inplace.domain.post.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentReadRepository {

    Page<CommentQueryResult.DetailedComment> findCommentsByPostId(
        Long postId,
        Long userId,
        Pageable pageable
    );
}
