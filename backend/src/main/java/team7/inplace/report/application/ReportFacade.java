package team7.inplace.report.application;

import lombok.RequiredArgsConstructor;
import team7.inplace.global.annotation.Facade;
import team7.inplace.post.application.PostService;

@Facade
@RequiredArgsConstructor
public class ReportFacade {

    private final PostService postService;
    private final ModerationService moderationService;

    public void processPostReport(Long postId) {
        String content = postService.getPostContentById(postId);
        boolean flag = moderationService.isContentFlagged(content);
        if (flag) {
            postService.deletePostSoftly(postId);
        }
    }

    public void processCommentReport(Long commentId) {
        String content = postService.getCommentContentById(commentId);
        boolean flag = moderationService.isContentFlagged(content);
        if (flag) {
            postService.deleteCommentSoftly(commentId);
        }
    }


}
