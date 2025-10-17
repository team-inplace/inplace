package my.inplace.application.alarm.event;

import my.inplace.application.alarm.event.dto.AlarmEvent;
import my.inplace.application.post.query.dto.CommentResult;
import my.inplace.domain.alarm.AlarmType;
import my.inplace.infra.alarm.FcmClient;
import my.inplace.application.alarm.command.AlarmCommandService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import my.inplace.application.post.query.PostQueryService;
import my.inplace.application.user.query.UserQueryService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmEventHandler {
    private static final String MENTION_TITLE = "새로운 언급 알림";
    private static final String MENTION_CONTENT = "%s 게시글에서 %s 님이 언급했습니다.";
    private static final String REPORT_TITLE = "%s 신고로 인한 삭제 알림";
    private static final String REPORT_CONTENT = "%s 게시글%s이 신고로 인하여 삭제되었습니다.";

    private final FcmClient fcmClient;
    private final AlarmCommandService alarmCommandService;
    private final UserQueryService userQueryService;
    private final PostQueryService postQueryService;

    @Async("fcmExecutor")
    public void processMentionAlarm(AlarmEvent mentionEvent) {
        Long receiverId = userQueryService.getUserIdByNickname(mentionEvent.receiver());
        
        String postTitle = postQueryService.getPostTitleById(mentionEvent.postId()).getTitle();
        String content = String.format(MENTION_CONTENT, postTitle, mentionEvent.sender());
        
        CommentResult.CommentIndex index = postQueryService.getCommentIndexByPostIdAndCommentId(
            mentionEvent.postId(), mentionEvent.commentId());
        
        alarmCommandService.saveAlarm(
            receiverId,
            mentionEvent.postId(),
            mentionEvent.commentId(),
            index.pageNumber(),
            index.offset(),
            content,
            AlarmType.MENTION
        );
        
        if(userQueryService.isMentionResented(receiverId)) {
            sendFcmMessage(MENTION_TITLE, content, userQueryService.getFcmTokenByUser(receiverId));
        }
    }

    @Async("fcmExecutor")
    public void processPostReportAlarm(AlarmEvent postReportAlarmEvent) {
        Long postId = postReportAlarmEvent.postId();
        Long userId = postQueryService.getPostAuthorIdById(postId);
        
        String title = String.format(REPORT_TITLE, "게시글");
        
        String postTitle = postQueryService.getPostTitleById(postId).getTitle();
        String content = String.format(REPORT_CONTENT, postTitle, "");
        
        alarmCommandService.saveAlarm(
            userId,
            postReportAlarmEvent.postId(),
            null,
            0,
            0,
            content,
            AlarmType.REPORT
        );
        
        if(userQueryService.isReportPushResented(userId)) {
            sendFcmMessage(title, content, userQueryService.getFcmTokenByUser(userId));
        }
    }

    @Async("fcmExecutor")
    public void processCommentReportAlarm(AlarmEvent commentReportAlarmEvent) {
        Long commentId = commentReportAlarmEvent.commentId();
        Long postId = postQueryService.getPostIdById(commentId);
        Long userId = postQueryService.getCommentAuthorIdById(commentId);
        
        String title = String.format(REPORT_TITLE, "댓글");
        
        String postTitle = postQueryService.getPostTitleById(postId).getTitle();
        String content = String.format(REPORT_CONTENT, postTitle, "에 작성한 댓글");
        
        alarmCommandService.saveAlarm(
            userId,
            postId,
            commentId,
            0,
            0,
            content,
            AlarmType.REPORT
        );
        
        if(userQueryService.isReportPushResented(userId)) {
            sendFcmMessage(title, content, userQueryService.getFcmTokenByUser(userId));
        }
    }

    public void sendFcmMessage(String title, String content, String fcmToken) {
        log.info("FCM 클라이언트 호출");
        try {
            fcmClient.sendMessageByToken(title, content, fcmToken);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
        }
    }
}
