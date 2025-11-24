package my.inplace.application.alarm.query;

import my.inplace.application.alarm.query.dto.AlarmResult;
import my.inplace.application.annotation.Facade;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import my.inplace.application.post.query.PostQueryService;
import my.inplace.domain.alarm.Alarm;
import my.inplace.security.util.AuthorizationUtil;

@Facade
@RequiredArgsConstructor
public class AlarmQueryFacade {
    private final AlarmQueryService alarmQueryService;
    private final PostQueryService postQueryService;
    
    public List<AlarmResult> getAlarmInfos() {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        
        List<Alarm> alarms = alarmQueryService.getAlarmInfos(userId);
        List<AlarmResult> alarmResults = new ArrayList<>();
        for (Alarm alarm : alarms) {
            Long index = postQueryService.getCommentIndexByPostIdAndCommentId(alarm.getPostId(), alarm.getCommentId());
            alarmResults.add(AlarmResult.from(alarm, index));
        }
        
        return alarmResults;
    }
    
    public void deleteAlarm(Long alarmId) {
        alarmQueryService.deleteAlarm(alarmId);
    }
}
