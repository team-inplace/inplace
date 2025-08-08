package post;

import alarm.dto.AlarmEvent;
import base.CursorResult;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import post.query.CommentQueryResult;
import post.query.PostQueryResult.DetailedPost;
import post.query.PostQueryResult.UserSuggestion;
import team7.inplace.global.annotation.Facade;
import team7.inplace.post.application.dto.PostCommand;
import team7.inplace.post.application.dto.PostCommand.CreatePost;
import team7.inplace.post.application.dto.PostCommand.UpdatePost;
import team7.inplace.post.application.dto.PostInfo;
import team7.inplace.security.util.AuthorizationUtil;
import user.command.UserCommandService;
import user.query.UserQueryService;

@Facade
@RequiredArgsConstructor
public class PostFacade {

    private final ApplicationEventPublisher eventPublisher;
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final PostService postService;

    public void createPost(CreatePost command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.createPost(command, userId);
        userCommandService.addToPostCount(userId, 1);
        userCommandService.updateUserTier(userId);
    }

    public void likePost(PostCommand.PostLike command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.likePost(command, userId);
    }

    public void updatePost(UpdatePost updateCommand) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.updatePost(updateCommand, userId);
    }

    @Transactional
    public void deletePost(Long postId) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.deletePost(postId, userId);
        userCommandService.addToPostCount(userId, -1);
        userCommandService.updateUserTier(userId);
    }

    @Transactional
    public void createComment(PostCommand.CreateComment command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        var commentId = postService.createComment(command, userId);
        Long authorId = postService.getAuthorIdByPostId(command.postId());
        userCommandService.addToReceivedCommentByUserId(authorId, 1);

        String mentioningUser = userQueryService.getUserInfo(userId).nickname();
        List<String> mentionedUsers = parseMentionedUser(command.comment());
        processMentionAlarm(command.postId(), commentId, mentioningUser, mentionedUsers);
    }

    public void likeComment(PostCommand.CommentLike command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.likeComment(command, userId);
    }

    public void updateComment(PostCommand.UpdateComment updateCommand) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.updateComment(updateCommand, userId);

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

    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.deleteComment(postId, commentId, userId);
        Long authorId = postService.getAuthorIdByPostId(postId);
        userQueryService.addToReceivedCommentByUserId(authorId, -1);
    }

    public CursorResult<DetailedPost> getPosts(Long cursorId, int size, String orderBy) {
        var userId = AuthorizationUtil.getUserIdOrNull();

        return postService.getPosts(userId, cursorId, size, orderBy);
    }

    public DetailedPost getPostById(Long postId) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        return postService.getPostById(postId, userId);
    }

    public PostInfo.PostImages getPostImageDetails(Long postId) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        return postService.getPostImageDetails(postId, userId);
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
