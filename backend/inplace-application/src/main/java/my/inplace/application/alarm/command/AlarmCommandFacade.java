package my.inplace.application.alarm.command;

import java.util.List;
import lombok.RequiredArgsConstructor;
import my.inplace.application.alarm.event.dto.AlarmEvent;
import my.inplace.application.alarm.query.AlarmQueryService;
import my.inplace.application.annotation.Facade;
import my.inplace.application.user.command.UserCommandService;
import my.inplace.domain.alarm.AlarmOutBox;
import my.inplace.domain.alarm.AlarmStatus;
import my.inplace.security.util.AuthorizationUtil;
import org.springframework.context.ApplicationEventPublisher;

@Facade
@RequiredArgsConstructor
public class AlarmCommandFacade {
    
    private final ApplicationEventPublisher eventPublisher;
    private final UserCommandService userCommandService;
    private final AlarmCommandService alarmCommandService;
    private final AlarmQueryService alarmQueryService;

    /* TODO: pending 조회후 상태 변경 필요한지 판단 필요
        updateAlarmToken이 실행된 후에 이벤트가 발행돼야함
     */
    public void updateAlarmToken(String fcmToken, String expoToken) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        
        userCommandService.updateAlarmToken(userId, fcmToken, expoToken);
        
        List<AlarmOutBox> alarmEvents = alarmQueryService.getAlarmEventByReceiverIdAndAlarmStatus(userId, AlarmStatus.PENDING);
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
