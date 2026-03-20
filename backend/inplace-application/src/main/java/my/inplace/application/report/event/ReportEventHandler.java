package my.inplace.application.report.event;

import lombok.RequiredArgsConstructor;
import my.inplace.application.post.command.PostCommandService;
import my.inplace.application.post.query.PostQueryService;
import my.inplace.application.report.event.dto.ModerationEvent;
import my.inplace.application.report.event.dto.ReportEvent.CommentReportEvent;
import my.inplace.application.report.event.dto.ReportEvent.PostReportEvent;
import my.inplace.application.report.query.ModerationService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


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
        Long postId = event.postId();
        String content = postQueryService.getPostContentById(postId);

        boolean flag = moderationService.isContentFlagged(content);
        if (!flag) return;

        Long receiverId = postQueryService.getAuthorIdByPostId(postId);
        String postTitle = postQueryService.getPostTitleById(postId).getTitle();

        postCommandService.deletePostSoftly(postId);

        eventPublisher.publishEvent(new ModerationEvent.PostDeleted(postId, receiverId, postTitle));
    }

    @Async("aiExecutor")
    @EventListener
    public void processCommentReport(CommentReportEvent event) {
        Long commentId = event.commentId();
        String content = postQueryService.getCommentContentById(commentId);

        boolean flag = moderationService.isContentFlagged(content);
        if (!flag) return;

        Long postId = postQueryService.getPostIdById(commentId);
        Long receiverId = postQueryService.getCommentAuthorIdById(commentId);
        String postTitle = postQueryService.getPostTitleById(postId).getTitle();

        postCommandService.deleteCommentSoftly(commentId);

        eventPublisher.publishEvent(
            new ModerationEvent.CommentDeleted(postId, commentId, receiverId, postTitle)
        );
    }
}
