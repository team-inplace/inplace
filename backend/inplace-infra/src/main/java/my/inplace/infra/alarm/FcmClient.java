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

@Slf4j
@Client
@RequiredArgsConstructor
public class FcmClient {
    private final FcmProperties fcmProperties;
    
    @PostConstruct
    public void initialize() throws IOException {
        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(fcmProperties.serviceAccountFile()).getInputStream()))
            .setProjectId(fcmProperties.projectId())
            .build();
        
        FirebaseApp.initializeApp(options);
    }
    
    public void sendMessageByToken(String title, String body, String token) throws FirebaseMessagingException{
        FirebaseMessaging.getInstance().send(Message.builder()
            .setNotification(Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())
            .setToken(token)
            .build());
    }
    
}
