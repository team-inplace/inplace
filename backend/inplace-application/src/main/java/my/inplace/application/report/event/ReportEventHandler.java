package my.inplace.application.report.event;

import lombok.RequiredArgsConstructor;
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
        }
    }

    @Async("aiExecutor")
    @EventListener
    public void processCommentReport(CommentReportEvent event) {
        String content = postQueryService.getCommentContentById(event.commentId());
        boolean flag = moderationService.isContentFlagged(content);
        if (flag) {
            postCommandService.deleteCommentSoftly(event.commentId());
        }
    }
}
