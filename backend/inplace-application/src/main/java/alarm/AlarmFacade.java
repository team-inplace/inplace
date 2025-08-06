package alarm;

import alarm.dto.AlarmInfo;
import annotation.Facade;
import java.util.List;
import lombok.RequiredArgsConstructor;
import team7.inplace.security.util.AuthorizationUtil;
import user.command.UserCommandService;

@Facade
@RequiredArgsConstructor
public class AlarmFacade {

    private final UserCommandService userCommandService;
    private final AlarmService alarmService;

    public void updateFcmToken(String token) {
        var userId = AuthorizationUtil.getUserIdOrThrow(); // TODO - security module

        userCommandService.updateFcmToken(userId, token);
    }

    public List<AlarmInfo> getAlarmInfos() {
        var userId = AuthorizationUtil.getUserIdOrThrow();

        return alarmService.getAlarmInfos(userId);
    }

    public void processAlarm(Long id) {
        alarmService.readAlarm(id);
    }
}
