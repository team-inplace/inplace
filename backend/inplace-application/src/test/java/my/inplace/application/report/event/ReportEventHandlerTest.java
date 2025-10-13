package my.inplace.application.report.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import my.inplace.application.alarm.event.dto.AlarmEvent.CommentReportAlarmEvent;
import my.inplace.application.alarm.event.dto.AlarmEvent.PostReportAlarmEvent;
import my.inplace.application.post.command.PostCommandService;
import my.inplace.application.post.query.PostQueryService;
import my.inplace.application.report.event.dto.ReportEvent.CommentReportEvent;
import my.inplace.application.report.event.dto.ReportEvent.PostReportEvent;
import my.inplace.application.report.query.ModerationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class ReportEventHandlerTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private PostQueryService postQueryService;

    @Mock
    private PostCommandService postCommandService;

    @Mock
    private ModerationService moderationService;

    @InjectMocks
    private ReportEventHandler reportEventHandler;

    @Test
    @DisplayName("적절 포스트 삭제x, 알람 이벤트 발행x")
    void processPostReport_notFlagged() {
        // given
        Long postId = 1L;
        String content = "normal content";
        PostReportEvent event = new PostReportEvent(postId);

        given(postQueryService.getPostContentById(postId)).willReturn(content);
        given(moderationService.isContentFlagged(content)).willReturn(false);

        // when
        reportEventHandler.processPostReport(event);

        // then
        verify(postCommandService, never()).deletePostSoftly(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("부적절 포스트 삭제, 알람 이벤트 발행")
    void processPostReport_flagged() {
        // given
        Long postId = 1L;
        String content = "flagged content";
        PostReportEvent event = new PostReportEvent(postId);

        given(postQueryService.getPostContentById(postId)).willReturn(content);
        given(moderationService.isContentFlagged(content)).willReturn(true);

        // when
        reportEventHandler.processPostReport(event);

        // then
        verify(postCommandService).deletePostSoftly(postId);
        verify(eventPublisher).publishEvent(any(PostReportAlarmEvent.class));
    }

    @Test
    @DisplayName("부적절 댓글 삭제, 알람 이벤트 발행")
    void processCommentReport_flagged() {
        // given
        Long commentId = 1L;
        String content = "flagged content";
        CommentReportEvent event = new CommentReportEvent(commentId);

        given(postQueryService.getCommentContentById(commentId)).willReturn(content);
        given(moderationService.isContentFlagged(content)).willReturn(true);

        // when
        reportEventHandler.processCommentReport(event);

        // then
        verify(postCommandService).deleteCommentSoftly(commentId);
        verify(eventPublisher).publishEvent(any(CommentReportAlarmEvent.class));
    }

}