package my.inplace.application.alarm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.inplace.application.alarm.event.dto.AlarmEvent;
import my.inplace.domain.alarm.AlarmOutBox;
import my.inplace.infra.alarm.ExpoClient;
import my.inplace.infra.alarm.FcmClient;
import my.inplace.infra.alarm.jpa.AlarmOutBoxRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
