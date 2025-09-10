package my.inplace.application.report.command;

import my.inplace.application.annotation.Facade;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import my.inplace.application.post.command.PostCommandService;
import my.inplace.application.report.event.dto.ReportEvent.CommentReportEvent;
import my.inplace.application.report.event.dto.ReportEvent.PostReportEvent;

@Facade
@RequiredArgsConstructor
public class ReportCommandFacade {
    private final ApplicationEventPublisher eventPublisher;
    private final PostCommandService postCommandService;

    public void processPostReport(Long postId) {
        postCommandService.reportPost(postId);
        eventPublisher.publishEvent(new PostReportEvent(postId));
    }

    public void processCommentReport(Long commentId) {
        postCommandService.reportComment(commentId);
        eventPublisher.publishEvent(new CommentReportEvent(commentId));
    }

}
