package my.inplace.application.alarm.query;

import my.inplace.application.alarm.query.dto.AlarmResult;
import my.inplace.application.annotation.Facade;
import lombok.RequiredArgsConstructor;

import java.util.List;
import my.inplace.security.util.AuthorizationUtil;

@Facade
@RequiredArgsConstructor
public class AlarmQueryFacade {
    private final AlarmQueryService alarmQueryService;
    
    public List<AlarmResult> getAlarmInfos() {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        
        return alarmQueryService.getAlarmInfos(userId);
    }
    
    public void deleteAlarm(Long alarmId) {
        alarmQueryService.deleteAlarm(alarmId);
    }
}
