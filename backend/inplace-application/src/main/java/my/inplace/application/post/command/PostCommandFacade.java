package my.inplace.application.post.command;

import my.inplace.application.annotation.Facade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import my.inplace.application.post.event.MentionPublisher;
import my.inplace.application.post.command.dto.PostCommand;
import my.inplace.application.post.query.PostQueryService;
import my.inplace.application.user.command.UserCommandService;
import my.inplace.application.user.query.UserQueryService;
import my.inplace.security.util.AuthorizationUtil;

@Facade
@RequiredArgsConstructor
@Transactional
public class PostCommandFacade {
    
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;
    
    private final MentionPublisher mentionPublisher;

    public void createPost(PostCommand.CreatePost command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postCommandService.createPost(command, userId);
        userCommandService.addToPostCount(userId, 1);
        userCommandService.updateUserTier(userId);
    }

    public void likePost(PostCommand.PostLike command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postCommandService.likePost(command, userId);
    }

    public void updatePost(PostCommand.UpdatePost updateCommand) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postCommandService.updatePost(updateCommand, userId);
    }

    public void deletePost(Long postId) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postCommandService.deletePost(postId, userId);
        userCommandService.addToPostCount(userId, -1);
        userCommandService.updateUserTier(userId);
    }

    public void createComment(PostCommand.CreateComment command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        var commentId = postCommandService.createComment(command, userId);
        Long authorId = postQueryService.getAuthorIdByPostId(command.postId());
        userCommandService.addToReceivedCommentByUserId(authorId, 1);
        String sender = userQueryService.getUserInfo(userId).nickname();
        
        mention(command.postId(), commentId, sender, command.comment());
    }
    
    public void updateComment(PostCommand.UpdateComment updateCommand) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postCommandService.updateComment(updateCommand, userId);
        String sender = userQueryService.getUserInfo(userId).nickname();
        
        mention(updateCommand.postId(), updateCommand.commentId(), sender, updateCommand.comment());
    }
    
    private void mention(Long postId, Long commentId, String sender, String commentContent) {
        mentionPublisher.processMention(postId, commentId, sender, commentContent);
    }

    public void likeComment(PostCommand.CommentLike command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postCommandService.likeComment(command, userId);
    }
    
    public void deleteComment(Long postId, Long commentId) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postCommandService.deleteComment(postId, commentId, userId);
        Long authorId = postQueryService.getAuthorIdByPostId(postId);
        userCommandService.addToReceivedCommentByUserId(authorId, -1);
    }

    public void deletePostSoftly(Long postId) {
        postCommandService.deletePostSoftly(postId);
    }

    public void unreportPost(Long postId) {
        postCommandService.unreportPost(postId);
    }

    public void deleteCommentSoftly(Long commentId) {
        postCommandService.deleteCommentSoftly(commentId);
    }

    public void unreportComment(Long commentId) {
        postCommandService.unreportComment(commentId);
    }
}
