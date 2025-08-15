package post.command;

import alarm.dto.AlarmEvent;
import annotation.Facade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import post.command.dto.PostCommand;
import post.query.PostQueryService;
import user.command.UserCommandService;
import user.query.UserQueryService;
import util.AuthorizationUtil;
import java.util.ArrayList;
import java.util.List;
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
        List<String> mentionedUsers = parseMentionedUser(command.comment());
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
        List<String> mentionedUsers = parseMentionedUser(updateCommand.comment());
        processMentionAlarm(updateCommand.postId(), updateCommand.commentId(), mentioningUser,
            mentionedUsers);
    }

    private List<String> parseMentionedUser(String comment) {
        List<String> mentions = new ArrayList<>();

        Pattern pattern = Pattern.compile("@(\\w+)");
        Matcher matcher = pattern.matcher(comment);
        while (matcher.find()) {
            mentions.add(matcher.group(1));
        }

        return mentions.stream()
                   .filter(userQueryService::isExistUserName)
                   .toList();
    }

    private void processMentionAlarm(
        Long postId, Long commentId, String mentioningUser, List<String> mentionedUsers) {
        for (String mentionedUser : mentionedUsers) {
            eventPublisher.publishEvent(
                new AlarmEvent.MentionAlarmEvent(postId, commentId, mentioningUser, mentionedUser));
        }
    }

    public void deleteComment(Long postId, Long commentId) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postCommandService.deleteComment(postId, commentId, userId);
        Long authorId = postQueryService.getAuthorIdByPostId(postId);
        userCommandService.addToReceivedCommentByUserId(authorId, -1);
    }
}
