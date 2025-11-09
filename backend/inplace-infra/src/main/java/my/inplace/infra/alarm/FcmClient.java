package my.inplace.infra.alarm;

import my.inplace.infra.annotation.Client;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import my.inplace.infra.properties.FcmProperties;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Client
@RequiredArgsConstructor
public class FcmClient {
    private final FcmProperties fcmProperties;
    
    @PostConstruct
    public void initialize() throws IOException {
        InputStream serviceAccount = new ClassPathResource(fcmProperties.serviceAccountFile()).getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        
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
    
    public void sendMessageByToken(String title, String body, String token) {
        try {
            String response = FirebaseMessaging.getInstance().send(Message.builder()
                .setNotification(Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build())
                .setToken(token)
                .build());
            log.info("FCM 알림 메시지 전송 성공, response={}", response);
        } catch (FirebaseMessagingException e) {
            log.error("FCM 알림 메시지 전송 실패 code={} msg={}", e.getErrorCode(), e.getMessage());
            throw new RuntimeException();
        }
    }
    
}
