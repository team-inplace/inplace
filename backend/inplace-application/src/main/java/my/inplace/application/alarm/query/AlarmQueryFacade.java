package my.inplace.application.alarm.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import my.inplace.application.alarm.query.dto.AlarmResult;
import my.inplace.application.annotation.Facade;
import my.inplace.security.util.AuthorizationUtil;

@Facade
@RequiredArgsConstructor
public class AlarmQueryFacade {
    private final AlarmQueryService alarmQueryService;

    public List<AlarmResult> getAlarms() {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        return alarmQueryService.getAlarms(userId);
    }
    
    public void deleteAlarm(Long alarmId) {
        alarmQueryService.deleteAlarm(alarmId);
    }
}
