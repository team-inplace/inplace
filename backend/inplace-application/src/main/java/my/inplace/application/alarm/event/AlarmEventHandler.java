package my.inplace.application.alarm.event;

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

    private final FcmClient fcmClient;
    private final AlarmCommandService alarmCommandService;
    private final UserQueryService userQueryService;
    private final PostQueryService postQueryService;

    @Async("fcmExecutor")
    public void processMentionAlarm(AlarmEvent mentionEvent) {
        Long receiverId = userQueryService.getUserIdByNickname(mentionEvent.receiver());
        String title = postQueryService.getPostTitleById(mentionEvent.postId()).getTitle();

        String content = title + " 게시글에서 " + mentionEvent.sender() + " 님이 언급했습니다.";
        
        Long index = postQueryService.getCommentIndexByPostIdAndCommentId(
            mentionEvent.postId(), mentionEvent.commentId());
        
        int pageNumber = index.intValue() / 10;
        int offset = index.intValue() % 10;
        
        if(userQueryService.isMentionResented(receiverId)) {
            sendFcmMessage("새로운 언급 알림", content, userQueryService.getFcmTokenByUser(receiverId));
        }
        
        alarmCommandService.saveAlarm(
            receiverId,
            mentionEvent.postId(),
            mentionEvent.commentId(),
            pageNumber,
            offset,
            content,
            AlarmType.MENTION
        );
    }

    @Async("fcmExecutor")
    public void processPostReportAlarm(AlarmEvent postReportAlarmEvent) {
        Long postId = postReportAlarmEvent.postId();
        Long userId = postQueryService.getPostAuthorIdById(postId);
        String title = postQueryService.getPostTitleById(postId).getTitle();

        String content = title + " 게시글이 신고로 인하여 삭제되었습니다.";

        if(userQueryService.isReportPushResented(userId)) {
            sendFcmMessage("게시글 신고로 인한 삭제 알림", content, userQueryService.getFcmTokenByUser(userId));
        }
        
        alarmCommandService.saveAlarm(
            userId,
            postReportAlarmEvent.postId(),
            null,
            0,
            0,
            content,
            AlarmType.REPORT
        );
    }

    @Async("fcmExecutor")
    public void processCommentReportAlarm(AlarmEvent commentReportAlarmEvent) {
        Long commentId = commentReportAlarmEvent.commentId();
        Long postId = postQueryService.getPostIdById(commentId);
        Long userId = postQueryService.getCommentAuthorIdById(commentId);
        String title = postQueryService.getPostTitleById(postId).getTitle();

        String content = title + " 게시글에 작성한 댓글이 신고로 인하여 삭제되었습니다";
    
        if(userQueryService.isReportPushResented(userId)) {
            sendFcmMessage("댓글 신고로 인한 삭제 알림", content, userQueryService.getFcmTokenByUser(userId));
        }

        alarmCommandService.saveAlarm(
            userId,
            postId,
            commentId,
            0,
            0,
            content,
            AlarmType.REPORT
        );
    }

    public void sendFcmMessage(String title, String content, String fcmToken) {
        log.info("alarm 보내기 시도");
        try {
            fcmClient.sendMessageByToken(title, content, fcmToken);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
        }
    }
}
