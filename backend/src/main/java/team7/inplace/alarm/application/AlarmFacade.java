package team7.inplace.alarm.application;

import lombok.RequiredArgsConstructor;
import team7.inplace.alarm.application.dto.AlarmInfo;
import team7.inplace.global.annotation.Facade;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.user.application.UserService;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class AlarmFacade {
    private final UserService userService;
    private final AlarmService alarmService;
    
    public void updateFcmToken(String token) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        
        userService.updateFcmToken(userId, token);
    }
    
    public List<AlarmInfo> getAlarmInfos() {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        
        return alarmService.getAlarmInfos(userId);
    }
    
    public void processAlarm(Long id) {
        alarmService.readAlarm(id);
    }
}
