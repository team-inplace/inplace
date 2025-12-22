package my.inplace.application.alarm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.inplace.application.alarm.event.dto.AlarmEvent;
import my.inplace.application.user.query.UserQueryService;
import my.inplace.domain.alarm.AlarmOutBox;
import my.inplace.infra.alarm.ExpoClient;
import my.inplace.infra.alarm.FcmClient;
import my.inplace.infra.alarm.jpa.AlarmOutBoxJpaRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmEventHandler {
    
    private final AlarmOutBoxJpaRepository alarmOutBoxJpaRepository;
    private final UserQueryService userQueryService;
    private final FcmClient fcmClient;
    private final ExpoClient expoClient;
    
    @Async("alarmExecutor")
    @EventListener
    @Transactional
    public void processMentionAlarm(AlarmEvent alarmEvent) {
        AlarmOutBox outBoxEvent = alarmOutBoxJpaRepository.findById(alarmEvent.id())
            .orElseThrow();
        
        String fcmToken = userQueryService.getFcmTokenByUser(outBoxEvent.getReceiverId());
        String expoToken = userQueryService.getExpoTokenByUserId(outBoxEvent.getReceiverId());
        if(fcmToken == null && expoToken == null) {
            outBoxEvent.pending();
            return;
        }
        
        boolean fcmSuccess = sendFcmMessage(outBoxEvent.getTitle(), outBoxEvent.getContent(), fcmToken);
        boolean expoSuccess = sendExpoMessage(outBoxEvent.getTitle(), outBoxEvent.getContent(), expoToken);
        
        if(fcmSuccess && expoSuccess) {
            outBoxEvent.published();
            return;
        }
        
        outBoxEvent.ready();
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
