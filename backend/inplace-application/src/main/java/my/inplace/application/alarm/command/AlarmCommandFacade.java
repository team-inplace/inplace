package my.inplace.application.alarm.command;

import lombok.RequiredArgsConstructor;
import my.inplace.application.annotation.Facade;
import my.inplace.application.user.command.UserCommandService;
import my.inplace.security.util.AuthorizationUtil;

@Facade
@RequiredArgsConstructor
public class AlarmCommandFacade {
    private final UserCommandService userCommandService;
    private final AlarmCommandService alarmCommandService;
    
    public void updateAlarmToken(String fcmToken, String expoToken) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        
        userCommandService.updateAlarmToken(userId, fcmToken, expoToken);
    }
    
    public void deleteFcmToken() {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        
        userCommandService.deleteFcmToken(userId);
    }
    
    public void processAlarm(Long id) {
        alarmCommandService.checkAlarm(id);
    }
}
