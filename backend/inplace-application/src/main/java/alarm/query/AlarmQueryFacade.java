package alarm.query;

import alarm.query.dto.AlarmResult;
import annotation.Facade;
import lombok.RequiredArgsConstructor;

import java.util.List;
import util.AuthorizationUtil;

@Facade
@RequiredArgsConstructor
public class AlarmQueryFacade {
    private final AlarmQueryService alarmQueryService;
    
    public List<AlarmResult> getAlarmInfos() {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        
        return alarmQueryService.getAlarmInfos(userId);
    }
}
