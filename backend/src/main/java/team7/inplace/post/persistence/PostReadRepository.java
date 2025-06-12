package team7.inplace.post.persistence;

import java.util.Optional;
import team7.inplace.global.cursor.CursorResult;
import team7.inplace.post.persistence.dto.PostQueryResult.DetailedPost;

public interface PostReadRepository {

    CursorResult<DetailedPost> findPostsOrderBy(
        Long userId,
        Long cursorId,
        int size,
        String orderBy
    );

    Optional<DetailedPost> findPostById(Long postId, Long userId);
}
