package alarm.event;

import alarm.AlarmType;
import alarm.FcmClient;
import alarm.command.AlarmCommandService;
import alarm.event.dto.AlarmEvent.CommentReportAlarmEvent;
import alarm.event.dto.AlarmEvent.MentionAlarmEvent;
import alarm.event.dto.AlarmEvent.PostReportAlarmEvent;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import post.query.PostQueryService;
import user.query.UserQueryService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmEventHandler {

    private final FcmClient fcmClient;
    private final AlarmCommandService alarmCommandService;
    private final UserQueryService userQueryService;
    private final PostQueryService postQueryService;

    @Async("fcmExecutor")
    @EventListener
    public void processMentionAlarm(MentionAlarmEvent mentionAlarmEvent) {
        Long userId = userQueryService.getUserIdByNickname(mentionAlarmEvent.receiver());
        String title = postQueryService.getPostTitleById(mentionAlarmEvent.postId());

        String content = title + " 게시글에서 " + mentionAlarmEvent.sender() + " 님이 언급했습니다.";

        sendFcmMessage(
            "새로운 언급 알림", content, userQueryService.getFcmTokenByUser(userId)
        );

        alarmCommandService.saveAlarm(
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
        Long userId = postQueryService.getPostAuthorIdById(postId);
        String title = postQueryService.getPostTitleById(postId);

        String content = title + " 게시글이 신고로 인하여 삭제되었습니다.";

        sendFcmMessage(
            "게시글 신고로 인한 삭제 알림", content, userQueryService.getFcmTokenByUser(userId)
        );

        alarmCommandService.saveAlarm(
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
        Long postId = postQueryService.getPostIdById(commentId);
        Long userId = postQueryService.getCommentAuthorIdById(commentId);
        String title = postQueryService.getPostTitleById(postId);

        String content = title + " 게시글에 작성한 댓글이 신고로 인하여 삭제되었습니다";

        sendFcmMessage(
            "댓글 신고로 인한 삭제 알림", content, userQueryService.getFcmTokenByUser(userId)
        );

        alarmCommandService.saveAlarm(
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
