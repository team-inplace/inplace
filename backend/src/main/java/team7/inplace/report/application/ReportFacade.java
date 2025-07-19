package team7.inplace.report.application;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import team7.inplace.global.annotation.Facade;
import team7.inplace.post.application.PostService;
import team7.inplace.report.event.ReportEvent.CommentReportEvent;
import team7.inplace.report.event.ReportEvent.PostReportEvent;

@Facade
@RequiredArgsConstructor
public class ReportFacade {
    private final ApplicationEventPublisher eventPublisher;
    private final PostService postService;

    public void processPostReport(Long postId) {
        postService.reportPost(postId);
        eventPublisher.publishEvent(new PostReportEvent(postId));
    }

    public void processCommentReport(Long commentId) {
        postService.reportComment(commentId);
        eventPublisher.publishEvent(new CommentReportEvent(commentId));
    }

}
