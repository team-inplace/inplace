package team7.inplace.report;

import static org.awaitility.Awaitility.await;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import team7.inplace.post.application.PostService;
import team7.inplace.report.application.ModerationService;
import team7.inplace.report.event.ReportEvent.CommentReportEvent;
import team7.inplace.report.event.ReportEvent.PostReportEvent;

@SpringBootTest
@EnableAsync
public class ReportEventHandlerTest {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @MockBean
    private PostService postService;

    @MockBean
    private ModerationService moderationService;

    @Test
    @DisplayName("게시글 이벤트 리스너 테스트")
    void postReportEventListenerTest() {
        // given
        Long postId = 1L;
        String content = "유해 콘텐츠";
        given(postService.getPostContentById(postId)).willReturn(content);
        given(moderationService.isContentFlagged(content)).willReturn(true);

        // when
        eventPublisher.publishEvent(new PostReportEvent(postId));

        // then
        await().atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                verify(postService).deletePostSoftly(postId);
            });
    }

    @Test
    @DisplayName("댓글 이벤트 리스너 테스트")
    void commentReportEventListenerTest() {
        // given
        Long commentId = 1L;
        String content = "유해 콘텐츠";
        given(postService.getCommentContentById(commentId)).willReturn(content);
        given(moderationService.isContentFlagged(content)).willReturn(true);

        // when
        eventPublisher.publishEvent(new CommentReportEvent(commentId));

        // then
        await().atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                verify(postService).deleteCommentSoftly(commentId);
            });
    }

}