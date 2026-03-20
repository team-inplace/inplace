package my.inplace.application.report.event;

import lombok.RequiredArgsConstructor;
import my.inplace.application.alarm.command.AlarmCommandService;
import my.inplace.application.alarm.util.ReportMessageFactory;
import my.inplace.application.report.event.dto.ModerationEvent;
import my.inplace.domain.alarm.AlarmType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ModerationEventHandler {

    private final AlarmCommandService alarmCommandService;
    private final ReportMessageFactory reportMessageFactory;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processPostDelete(ModerationEvent.PostDeleted event) {
        String title = reportMessageFactory.createPostTitle();
        String content = reportMessageFactory.createPostMessage(event.postTitle());

        alarmCommandService.saveAlarm(event.receiverId(), event.postId(), null, content, AlarmType.REPORT);

        alarmCommandService.saveAlarmEvent(event.receiverId(), title, content, event.postId(), null, AlarmType.REPORT);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processCommentDelete(ModerationEvent.CommentDeleted event) {
        String title = reportMessageFactory.createCommentTitle();
        String content = reportMessageFactory.createCommentMessage(event.postTitle());

        alarmCommandService.saveAlarm(event.receiverId(), event.postId(), event.commentId(), content, AlarmType.REPORT);

        alarmCommandService.saveAlarmEvent(event.receiverId(), title, content, event.postId(), event.commentId(), AlarmType.REPORT);
    }

}
