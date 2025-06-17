package team7.inplace.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team7.inplace.global.annotation.Facade;
import team7.inplace.global.cursor.CursorResult;
import team7.inplace.post.application.dto.PostCommand;
import team7.inplace.post.application.dto.PostCommand.CreatePost;
import team7.inplace.post.application.dto.PostCommand.UpdatePost;
import team7.inplace.post.persistence.dto.CommentQueryResult;
import team7.inplace.post.persistence.dto.PostQueryResult.DetailedPost;
import team7.inplace.security.util.AuthorizationUtil;

@Facade
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;

    public void createPost(CreatePost command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.createPost(command, userId);
    }

    public void updatePost(UpdatePost updateCommand) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.updatePost(updateCommand, userId);
    }

    public void deletePost(Long postId) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.deletePost(postId, userId);
    }

    public void createComment(PostCommand.CreateComment command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.createComment(command, userId);
    }

    public void updateComment(PostCommand.UpdateComment updateCommand) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.updateComment(updateCommand, userId);
    }

    public void deleteComment(Long postId, Long commentId) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.deleteComment(postId, commentId, userId);
    }

    public CursorResult<DetailedPost> getPosts(Long cursorId, int size, String orderBy) {
        var userId = AuthorizationUtil.getUserIdOrThrow();

        return postService.getPosts(userId, cursorId, size, orderBy);
    }

    public DetailedPost getPostById(Long postId) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        return postService.getPostById(postId, userId);
    }

    public Page<CommentQueryResult.DetailedComment> getCommentsByPostId(
        Long postId, Pageable pageable
    ) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        return postService.getCommentsByPostId(postId, userId, pageable);
    }
}
