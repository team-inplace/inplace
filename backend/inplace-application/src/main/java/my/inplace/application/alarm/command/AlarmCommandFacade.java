package my.inplace.application.alarm.command;

import my.inplace.application.annotation.Facade;
import lombok.RequiredArgsConstructor;
import my.inplace.application.user.command.UserCommandService;
import my.inplace.security.util.AuthorizationUtil;

@Facade
@RequiredArgsConstructor
public class AlarmCommandFacade {
    private final UserCommandService userCommandService;
    private final AlarmCommandService alarmCommandService;
    
    public void updateFcmToken(String token) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        
        userCommandService.updateFcmToken(userId, token);
    }
    
    public void processAlarm(Long id) {
        alarmCommandService.checkAlarm(id);
    }
}
