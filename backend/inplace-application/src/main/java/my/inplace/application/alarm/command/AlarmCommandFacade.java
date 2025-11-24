package my.inplace.application.alarm.command;

import my.inplace.application.alarm.event.dto.AlarmEvent;
import my.inplace.application.alarm.query.AlarmQueryService;
import my.inplace.application.annotation.Facade;
import lombok.RequiredArgsConstructor;
import my.inplace.application.user.command.UserCommandService;
import my.inplace.domain.alarm.AlarmOutBox;
import my.inplace.security.util.AuthorizationUtil;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class AlarmCommandFacade {
    
    private final ApplicationEventPublisher eventPublisher;
    private final UserCommandService userCommandService;
    private final AlarmCommandService alarmCommandService;
    private final AlarmQueryService alarmQueryService;
    
    public void updateAlarmToken(String fcmToken, String expoToken) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        
        userCommandService.updateAlarmToken(userId, fcmToken, expoToken);
        
        List<AlarmOutBox> alarmEvents = alarmQueryService.getAlarmEventByReceiverId(userId);
        for (AlarmOutBox alarmEvent : alarmEvents) {
            eventPublisher.publishEvent(AlarmEvent.from(alarmEvent));
        }
    }
    
    public void deleteFcmToken() {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        
        userCommandService.deleteFcmToken(userId);
    }
    
    public void processAlarm(Long id) {
        alarmCommandService.checkAlarm(id);
    }
}
