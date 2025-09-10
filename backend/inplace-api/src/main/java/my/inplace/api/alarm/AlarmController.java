package my.inplace.api.alarm;

import my.inplace.application.alarm.command.AlarmCommandFacade;
import my.inplace.api.alarm.dto.AlarmRequest;
import my.inplace.api.alarm.dto.AlarmResponse;
import my.inplace.application.alarm.query.AlarmQueryFacade;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {

    private final AlarmCommandFacade alarmCommandFacade;
    private final AlarmQueryFacade alarmQueryFacade;

    // 로그인 순간에 FCM 토큰 받기
    @PostMapping
    public ResponseEntity<Void> upsertFcmToken(
        @RequestBody AlarmRequest alarmRequest
    ) {
        alarmCommandFacade.updateFcmToken(alarmRequest.token());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AlarmResponse>> getAlarmList() {
        var response = alarmQueryFacade.getAlarmInfos().stream()
            .map(AlarmResponse::from)
            .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> processOneAlarm(
        @PathVariable Long id
    ) {
        alarmCommandFacade.processAlarm(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
