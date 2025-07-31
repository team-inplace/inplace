package team7.inplace.alarm;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import team7.inplace.alarm.application.AlarmService;
import team7.inplace.alarm.application.FcmClient;
import team7.inplace.alarm.domain.AlarmType;
import team7.inplace.alarm.event.AlarmEvent;
import team7.inplace.alarm.event.AlarmEvent.MentionAlarmEvent;
import team7.inplace.post.application.PostService;
import team7.inplace.user.application.UserService;
import team7.inplace.user.application.dto.UserCommand;
import team7.inplace.user.domain.Role;
import team7.inplace.user.domain.User;
import team7.inplace.user.domain.UserType;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@EnableAsync
public class AlarmEventHandlerTest {
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @MockBean
    private UserService userService;
    @MockBean
    private FcmClient fcmClient;
    @MockBean
    private AlarmService alarmService;
    @MockBean
    private PostService postService;
    
    @Test
    @DisplayName("멘션 이벤트 테스트")
    void test1() {
        // given
        String sender = "유저1";
        String username = "유저2";
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 2L;
        
        User user = new User(username, "", "유저2", "", UserType.KAKAO, Role.USER);
        ReflectionTestUtils.setField(user, "id", 1L);
        UserCommand.Info info = UserCommand.Info.of(user);
        given(postService.getPostTitleById(postId)).willReturn("제목");
        given(userService.getUserByUsername(username)).willReturn(info);
        given(userService.getFcmTokenByUser(userId)).willReturn("fcm-token");
        
        MentionAlarmEvent event = new MentionAlarmEvent(postId, commentId, sender, username);
        
        // when
        eventPublisher.publishEvent(event);
        
        // then
        await().atMost(3, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(fcmClient).sendMessageByToken(
                eq("새로운 언급 알림"),
                contains("제목 게시글에서 유저1 님이 언급했습니다."),
                eq("fcm-token")
            );
            
            verify(alarmService).saveAlarm(
                eq(userId),
                eq(postId),
                eq(commentId),
                contains("언급"),
                eq(AlarmType.MENTION)
            );
        });
    }
    
    @Test
    @DisplayName("게시글 신고 삭제 이벤트 테스트")
    void test2() {
        // given
        Long postId = 1L;
        Long userId = 2L;
        
        given(postService.getPostTitleById(postId)).willReturn("제목");
        given(postService.getPostAuthorIdById(postId)).willReturn(userId);
        given(userService.getFcmTokenByUser(userId)).willReturn("fcm-token");
        
        AlarmEvent.PostReportAlarmEvent event = new AlarmEvent.PostReportAlarmEvent(postId);
        
        // when
        eventPublisher.publishEvent(event);
        
        // then
        await().atMost(3, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(fcmClient).sendMessageByToken(
                eq("게시글 신고로 인한 삭제 알림"),
                contains("제목 게시글이 신고로 인하여 삭제되었습니다."),
                eq("fcm-token")
            );
            
            verify(alarmService).saveAlarm(
                eq(userId),
                eq(postId),
                isNull(),
                contains("삭제되었습니다"),
                eq(AlarmType.REPORT)
            );
        });
    }
    
    @Test
    @DisplayName("댓글 신고 삭제 이벤트 테스트")
    void test3() {
        Long commentId = 1L;
        Long postId = 1L;
        Long userId = 2L;
        
        given(postService.getPostTitleById(postId)).willReturn("제목");
        given(postService.getPostIdById(commentId)).willReturn(postId);
        given(postService.getCommentAuthorIdById(commentId)).willReturn(userId);
        given(userService.getFcmTokenByUser(userId)).willReturn("fcm-token");
        
        AlarmEvent.CommentReportAlarmEvent event = new AlarmEvent.CommentReportAlarmEvent(commentId);
        
        // when
        eventPublisher.publishEvent(event);
        
        // then
        await().atMost(3, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(fcmClient).sendMessageByToken(
                eq("댓글 신고로 인한 삭제 알림"),
                contains("제목 게시글에 작성한 댓글이 신고로 인하여 삭제되었습니다"),
                eq("fcm-token")
            );
            
            verify(alarmService).saveAlarm(
                eq(userId),
                eq(postId),
                eq(commentId),
                contains("댓글이 신고로 인하여 삭제되었습니다"),
                eq(AlarmType.REPORT)
            );
        });
    }
}
