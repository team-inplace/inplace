package my.inplace.infra.alarm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.inplace.domain.alarm.AlarmOutBox;
import my.inplace.infra.annotation.Client;
import my.inplace.infra.properties.FcmProperties;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Client
@RequiredArgsConstructor
public class FcmClient {
    private final FcmProperties fcmProperties;
    
    @PostConstruct
    public void initialize() throws IOException {
        InputStream serviceAccount = new ClassPathResource(fcmProperties.serviceAccountFile()).getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

        // TODO: 에러코드 커스텀으로 변경
        if(credentials == null || fcmProperties.projectId() == null) {
            throw new IllegalStateException("Firebase 초기화 정보가 올바르지 않습니다.");
        }
        
        FirebaseOptions options = FirebaseOptions.builder()
                                      .setCredentials(credentials)
                                      .setProjectId(fcmProperties.projectId())
                                      .build();
        
        if(FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
    
    public void sendMessageByToken(AlarmOutBox alarmOutBox, String token) {
        Message.Builder builder = Message.builder()
            .setNotification(Notification.builder()
                .setTitle(alarmOutBox.getTitle())
                .setBody(alarmOutBox.getContent())
                .build())
            .putData("alarmId", String.valueOf(alarmOutBox.getAlarmId()))
            .setToken(token);

        applyFcmData(builder, alarmOutBox);

        try {
            Message message = builder.build();
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("FCM 메세지 전송 성공 : request={} response={}", message, response);
        } catch (FirebaseMessagingException e) {
            log.error("FCM 알림 메시지 전송 실패 code={} msg={}", e.getErrorCode(), e.getMessage());
            throw new RuntimeException();
        }
    }

    private void applyFcmData(Message.Builder builder, AlarmOutBox alarmOutBox) {
        switch (alarmOutBox.getAlarmType()) {
            case MENTION -> {
                builder.putData("postId", String.valueOf(alarmOutBox.getPostId()));
                builder.putData("commentId", String.valueOf(alarmOutBox.getCommentId()));
            }
            case REPORT -> {
            }
        }
    }
    
}
