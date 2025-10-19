package my.inplace.application.post.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import my.inplace.application.annotation.Facade;
import my.inplace.application.post.query.dto.CommentResult;
import my.inplace.application.post.query.dto.PostResult;
import my.inplace.common.cursor.CursorResult;
import my.inplace.security.util.AuthorizationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryFacade {

    private final PostQueryService postQueryService;

    public CursorResult<PostResult.DetailedPost> getPosts(Long cursorValue, Long cursorId, int size, String orderBy) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var queryResult = postQueryService.getPosts(userId, cursorValue, cursorId, size, orderBy);
        var result = queryResult.value().stream()
            .map(PostResult.DetailedPost::from)
            .toList();

        return new CursorResult<>(
            result,
            queryResult.hasNext(),
            queryResult.nextCursorValue(),
            queryResult.nextCursorId()
        );
    }

    public PostResult.DetailedPost getPostById(Long postId) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var queryResult = postQueryService.getPostById(postId, userId);
        return PostResult.DetailedPost.from(queryResult);
    }

    public PostResult.PostImages getPostImageDetails(Long postId) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        return postQueryService.getPostImageDetails(postId, userId);
    }

    public Page<CommentResult.DetailedComment> getCommentsByPostId(
        Long postId, Pageable pageable
    ) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var queryResult = postQueryService.getCommentsByPostId(postId, userId, pageable);
        return queryResult.map(CommentResult.DetailedComment::from);
    }

    public List<PostResult.UserSuggestion> getCommentUserSuggestions(Long postId, String keyword) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        var queryResult = postQueryService.getUserSuggestions(userId, postId, keyword);

        return queryResult.stream()
            .map(PostResult.UserSuggestion::from)
            .toList();
    }

    public List<PostResult.ReportedPost> findReportedPosts() {
        return postQueryService.findReportedPost();
    }

    public List<PostResult.ReportedComment> findReportedComments() {
        return postQueryService.findReportedComment();
    }
}
