package my.inplace.application.post.command;

import my.inplace.application.alarm.command.AlarmCommandService;
import my.inplace.application.annotation.Facade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import my.inplace.application.post.util.MentionMessageFactory;
import my.inplace.application.post.util.ReceiverParser;
import my.inplace.application.post.command.dto.PostCommand;
import my.inplace.application.post.query.PostQueryService;
import my.inplace.application.user.command.UserCommandService;
import my.inplace.application.user.query.UserQueryService;
import my.inplace.domain.alarm.AlarmType;
import my.inplace.security.util.AuthorizationUtil;

import java.util.List;

@Facade
@RequiredArgsConstructor
@Transactional
public class PostCommandFacade {
    
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;
    
    private final AlarmCommandService alarmCommandService;
    private final ReceiverParser receiverParser;
    private final MentionMessageFactory mentionMessageFactory;
    
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
        
        mention(command.postId(), commentId, userId, command.comment());
    }
    
    public void updateComment(PostCommand.UpdateComment updateCommand) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postCommandService.updateComment(updateCommand, userId);
        
        mention(updateCommand.postId(), updateCommand.commentId(), userId, updateCommand.comment());
    }
    
    private void mention(Long postId, Long commentId, Long senderId, String commentContent) {
        List<Long> receiverIds = receiverParser.parseMentionedUser(commentContent).stream()
            .map(userQueryService::getUserIdByNickname)
            .toList();
        
        String title = mentionMessageFactory.createTitle();
        
        for (Long receiverId : receiverIds) {
            Long index = postQueryService.getCommentIndexByPostIdAndCommentId(postId, commentId);
            String fcmToken = userQueryService.getFcmTokenByUser(receiverId);
            String expoToken = userQueryService.getExpoTokenByUserId(receiverId);
            
            String content = mentionMessageFactory.createMessage(
                postQueryService.getPostTitleById(postId).getTitle(),
                userQueryService.getUserInfo(receiverId).nickname());
            
            // 비즈니스 데이터 저장
            alarmCommandService.saveAlarm(
                receiverId,
                postId,
                commentId,
                (int) (index / 10),
                (int) (index % 10),
                content,
                AlarmType.MENTION);
            
            // 이벤트 데이터 저장
            alarmCommandService.saveAlarmEvent(title, content, fcmToken, expoToken);
        }
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
