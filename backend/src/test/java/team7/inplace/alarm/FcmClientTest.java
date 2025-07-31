package team7.inplace.alarm;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team7.inplace.alarm.application.FcmClient;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FcmClientTest {
    @Autowired
    private FcmClient fcmClient;
    
    @Test
    public void sendMessageByToken_realRequest() throws Exception {
        String testToken = "실제 FCM 토큰";
        
        assertThrows(FirebaseMessagingException.class, () ->
            fcmClient.sendMessageByToken("테스트 제목", "테스트 본문", testToken)
        );
    }
}
