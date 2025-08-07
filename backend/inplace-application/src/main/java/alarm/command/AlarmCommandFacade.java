package alarm.command;

import annotation.Facade;
import lombok.RequiredArgsConstructor;
import user.command.UserCommandService;

@Facade
@RequiredArgsConstructor
public class AlarmCommandFacade {
    private final UserCommandService userCommandService;
    private final AlarmCommandService alarmCommandService;
    
    public void updateFcmToken(String token) {
        var userId = AuthorizationUtil.getUserIdOrThrow(); // TODO - security module
        
        userCommandService.updateFcmToken(userId, token);
    }
    
    public void processAlarm(Long id) {
        alarmCommandService.checkAlarm(id);
    }
}
