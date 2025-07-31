package team7.inplace.alarm.application;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import team7.inplace.alarm.domain.AlarmType;
import team7.inplace.alarm.event.AlarmEvent.PostReportAlarmEvent;
import team7.inplace.alarm.event.AlarmEvent.CommentReportAlarmEvent;
import team7.inplace.alarm.event.AlarmEvent.MentionAlarmEvent;
import team7.inplace.post.application.PostService;
import team7.inplace.user.application.UserService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmEventHandler {
    private final FcmClient fcmClient;
    private final AlarmService alarmService;
    private final UserService userService;
    private final PostService postService;
    
    @Async("fcmExecutor")
    @EventListener
    public void processMentionAlarm(MentionAlarmEvent mentionAlarmEvent) {
        Long userId = userService.getUserByUsername(mentionAlarmEvent.receiver()).id();
        String title = postService.getPostTitleById(mentionAlarmEvent.postId());
        
        String content = title + " 게시글에서 " + mentionAlarmEvent.sender() + " 님이 언급했습니다.";
        
        sendFcmMessage(
            "새로운 언급 알림", content, userService.getFcmTokenByUser(userId)
        );
        
        alarmService.saveAlarm(
            userId,
            mentionAlarmEvent.postId(),
            mentionAlarmEvent.commentId(),
            content,
            AlarmType.MENTION
        );
    }
    
    @Async("fcmExecutor")
    @EventListener
    public void processPostReportAlarm(PostReportAlarmEvent postReportAlarmEvent) {
        Long postId = postReportAlarmEvent.postId();
        Long userId = postService.getPostAuthorIdById(postId);
        String title = postService.getPostTitleById(postId);
        
        String content = title + " 게시글이 신고로 인하여 삭제되었습니다.";
        
        sendFcmMessage(
            "게시글 신고로 인한 삭제 알림", content, userService.getFcmTokenByUser(userId)
        );
        
        alarmService.saveAlarm(
            userId,
            postReportAlarmEvent.postId(),
            null,
            content,
            AlarmType.REPORT
        );
    }
    
    @Async("fcmExecutor")
    @EventListener
    public void processPostReportAlarm(CommentReportAlarmEvent commentReportAlarmEvent) {
        Long commentId = commentReportAlarmEvent.commentId();
        Long postId = postService.getPostIdById(commentId);
        Long userId = postService.getCommentAuthorIdById(commentId);
        String title = postService.getPostTitleById(postId);
        
        String content = title + " 게시글에 작성한 댓글이 신고로 인하여 삭제되었습니다";
        
        sendFcmMessage(
            "댓글 신고로 인한 삭제 알림", content, userService.getFcmTokenByUser(userId)
        );
        
        alarmService.saveAlarm(
            userId,
            postId,
            commentId,
            content,
            AlarmType.REPORT
        );
    }
    
    private void sendFcmMessage(String title, String content, String fcmToken) {
        try {
            fcmClient.sendMessageByToken(title, content, fcmToken);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
        }
    }
}
