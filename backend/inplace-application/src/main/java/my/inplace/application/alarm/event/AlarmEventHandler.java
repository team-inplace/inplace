package my.inplace.application.alarm.event;

import my.inplace.application.alarm.event.dto.AlarmEvent;
import my.inplace.application.post.query.dto.CommentResult;
import my.inplace.domain.alarm.AlarmOutBox;
import my.inplace.domain.alarm.AlarmType;
import my.inplace.infra.alarm.ExpoClient;
import my.inplace.infra.alarm.FcmClient;
import my.inplace.application.alarm.command.AlarmCommandService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.inplace.infra.alarm.jpa.AlarmOutBoxRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import my.inplace.application.post.query.PostQueryService;
import my.inplace.application.user.query.UserQueryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmEventHandler {
    
    private final AlarmOutBoxRepository alarmOutBoxRepository;
    private final FcmClient fcmClient;
    private final ExpoClient expoClient;

    @Async("alarmExecutor")
    @EventListener
    @Transactional
    public void processMentionAlarm(AlarmEvent alarmEvent) {
        AlarmOutBox outBoxEvent = alarmOutBoxRepository.findById(alarmEvent.idempotencyKey())
            .orElseThrow();
        
        boolean fcmSuccess = sendFcmMessage(alarmEvent.title(), alarmEvent.body(), alarmEvent.fcmToken());
        boolean expoSuccess = sendExpoMessage(alarmEvent.title(), alarmEvent.body(), alarmEvent.expoToken());
        
        if(fcmSuccess && expoSuccess) {
            outBoxEvent.published();
            return;
        }
        
        outBoxEvent.pending();
    }
    
    public boolean sendFcmMessage(String title, String body, String token) {
        if (token == null) return true;
        
        try {
            fcmClient.sendMessageByToken(title, body, token);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
    
    public boolean sendExpoMessage(String title, String body, String token) {
        if (token == null) return true;
        
        try {
            expoClient.sendMessageByToken(title, body, token);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
    
}
