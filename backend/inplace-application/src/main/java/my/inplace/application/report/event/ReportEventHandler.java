package my.inplace.application.report.event;

import lombok.RequiredArgsConstructor;
import my.inplace.application.alarm.event.AlarmEventBuilder;
import my.inplace.domain.alarm.AlarmType;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import my.inplace.application.post.command.PostCommandService;
import my.inplace.application.post.query.PostQueryService;
import my.inplace.application.report.query.ModerationService;
import my.inplace.application.report.event.dto.ReportEvent.CommentReportEvent;
import my.inplace.application.report.event.dto.ReportEvent.PostReportEvent;


@Component
@RequiredArgsConstructor
public class ReportEventHandler {

    private final ApplicationEventPublisher eventPublisher;
    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;
    private final ModerationService moderationService;

    @Async("aiExecutor")
    @EventListener
    public void processPostReport(PostReportEvent event) {
        String content = postQueryService.getPostContentById(event.postId());
        boolean flag = moderationService.isContentFlagged(content);
        if (flag) {
            postCommandService.deletePostSoftly(event.postId());
            processReportAlarm(event.postId(), null);
        }
    }

    @Async("aiExecutor")
    @EventListener
    public void processCommentReport(CommentReportEvent event) {
        String content = postQueryService.getCommentContentById(event.commentId());
        boolean flag = moderationService.isContentFlagged(content);
        if (flag) {
            postCommandService.deleteCommentSoftly(event.commentId());
            processReportAlarm(null, event.commentId());
        }
    }
    
    private void processReportAlarm(Long postId, Long commentId) {
        AlarmEventBuilder builder = AlarmEventBuilder.create();
        
        if(commentId == null) {
            builder.postId(postId)
                .type(AlarmType.REPORT)
                .publish(eventPublisher);
            return;
        }
        
        builder.commentId(commentId)
            .type(AlarmType.REPORT)
            .publish(eventPublisher);
    }
}
