package my.inplace.application.post.command;

import my.inplace.application.alarm.event.dto.AlarmEvent;
import my.inplace.application.annotation.Facade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import my.inplace.application.post.command.dto.PostCommand;
import my.inplace.application.post.query.PostQueryService;
import my.inplace.application.user.command.UserCommandService;
import my.inplace.application.user.query.UserQueryService;
import my.inplace.security.util.AuthorizationUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Facade
@RequiredArgsConstructor
@Transactional
public class PostCommandFacade {

    private final ApplicationEventPublisher eventPublisher;

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

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

        String mentioningUser = userQueryService.getUserInfo(userId).nickname();
        Map<Long, String> mentionedUsers = parseMentionedUser(command.comment());
        processMentionAlarm(command.postId(), commentId, mentioningUser, mentionedUsers);
    }

    public void likeComment(PostCommand.CommentLike command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postCommandService.likeComment(command, userId);
    }

    public void updateComment(PostCommand.UpdateComment updateCommand) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postCommandService.updateComment(updateCommand, userId);

        String mentioningUser = userQueryService.getUserInfo(userId).nickname();
        var mentionedUsers = parseMentionedUser(updateCommand.comment());
        processMentionAlarm(updateCommand.postId(), updateCommand.commentId(), mentioningUser, mentionedUsers);
    }

    private Map<Long, String> parseMentionedUser(String comment) {
        Map<Long, String> mentions = new HashMap<>();
        

        Pattern pattern = Pattern.compile("<<@([^:>]+):([^>]+)>>");
        Matcher matcher = pattern.matcher(comment);
        while (matcher.find()) {
            mentions.put(
                Long.parseLong(matcher.group(1)), matcher.group(2));
        }

        return mentions;
    }

    private void processMentionAlarm(
        Long postId, Long commentId, String mentioningUser, Map<Long, String> mentionedUsers) {
        for (Map.Entry<Long, String> entry : mentionedUsers.entrySet()) {
            eventPublisher.publishEvent(
                new AlarmEvent.MentionAlarmEvent(
                    postId, commentId, mentioningUser, entry.getKey(), entry.getValue())
            );
        }
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
