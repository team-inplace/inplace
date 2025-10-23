package my.inplace.domain.post.query;

import my.inplace.common.cursor.CursorResult;
import java.util.List;
import java.util.Optional;

public interface PostReadRepository {

    CursorResult<PostQueryResult.DetailedPost> findPostsOrderBy(
        Long userId,
        Long cursorValue,
        Long cursorId,
        int size,
        String orderBy
    );

    Optional<PostQueryResult.DetailedPost> findPostById(Long postId, Long userId);

    List<PostQueryResult.UserSuggestion> findCommentUserSuggestions(Long postId, String keyword);
}
