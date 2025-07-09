package team7.inplace.report.application.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import team7.inplace.post.application.PostService;
import team7.inplace.report.application.ModerationService;
import team7.inplace.report.event.ReportEvent.CommentReportEvent;
import team7.inplace.report.event.ReportEvent.PostReportEvent;

@Component
@RequiredArgsConstructor
public class ReportEventHandler {

    private final PostService postService;
    private final ModerationService moderationService;

    @Async("aiExecutor")
    @EventListener
    public void processPostReport(PostReportEvent event) {
        String content = postService.getPostContentById(event.postId());
        boolean flag = moderationService.isContentFlagged(content);
        if (flag) {
            postService.deletePostSoftly(event.postId());
        }
    }

    @Async("aiExecutor")
    @EventListener
    public void processCommentReport(CommentReportEvent event) {
        String content = postService.getCommentContentById(event.commentId());
        boolean flag = moderationService.isContentFlagged(content);
        if (flag) {
            postService.deleteCommentSoftly(event.commentId());
        }
    }
}