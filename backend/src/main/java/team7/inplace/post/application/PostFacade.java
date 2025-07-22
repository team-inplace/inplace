package team7.inplace.post.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.annotation.Facade;
import team7.inplace.global.cursor.CursorResult;
import team7.inplace.post.application.dto.PostCommand;
import team7.inplace.post.application.dto.PostCommand.CreatePost;
import team7.inplace.post.application.dto.PostCommand.UpdatePost;
import team7.inplace.post.persistence.dto.CommentQueryResult;
import team7.inplace.post.persistence.dto.PostQueryResult.DetailedPost;
import team7.inplace.post.persistence.dto.PostQueryResult.UserSuggestion;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.user.application.UserService;

@Facade
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;
    private final UserService userService;

    @Transactional
    public void createPost(CreatePost command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.createPost(command, userId);
        userService.addToPostCount(userId, 1);
        userService.updateUserTier(userId);
    }

    public void updatePost(UpdatePost updateCommand) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.updatePost(updateCommand, userId);
    }

    @Transactional
    public void deletePost(Long postId) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.deletePost(postId, userId);
        userService.addToPostCount(userId, -1);
        userService.updateUserTier(userId);
    }

    @Transactional
    public void createComment(PostCommand.CreateComment command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.createComment(command, userId);
        Long authorId = postService.getAuthorIdByPostId(command.postId());
        userService.addToReceivedCommentByUserId(authorId, 1);
    }

    public void updateComment(PostCommand.UpdateComment updateCommand) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.updateComment(updateCommand, userId);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.deleteComment(postId, commentId, userId);
        Long authorId = postService.getAuthorIdByPostId(postId);
        userService.addToReceivedCommentByUserId(authorId, -1);
    }

    public CursorResult<DetailedPost> getPosts(Long cursorId, int size, String orderBy) {
        var userId = AuthorizationUtil.getUserIdOrNull();

        return postService.getPosts(userId, cursorId, size, orderBy);
    }

    public DetailedPost getPostById(Long postId) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        return postService.getPostById(postId, userId);
    }

    public Page<CommentQueryResult.DetailedComment> getCommentsByPostId(
        Long postId, Pageable pageable
    ) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        return postService.getCommentsByPostId(postId, userId, pageable);
    }

    public List<UserSuggestion> getCommentUserSuggestions(Long postId, String keyword) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        return postService.getUserSuggestions(userId, postId, keyword);
    }
}
