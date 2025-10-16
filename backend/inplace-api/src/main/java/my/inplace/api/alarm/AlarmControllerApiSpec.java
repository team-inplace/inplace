package my.inplace.api.alarm;

import io.swagger.v3.oas.annotations.Operation;
import my.inplace.api.alarm.dto.AlarmRequest;
import my.inplace.api.alarm.dto.AlarmResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface AlarmControllerApiSpec {
    
    @Operation(summary = "FCM 토큰 입력", description = "로그인 시에 FCM 토큰을 등록합니다.")
    public ResponseEntity<Void> upsertFcmToken(@RequestBody AlarmRequest alarmRequest);
    
    @Operation(summary = "FCM 토큰 삭제", description = "로그아웃 시에 FCM 토큰을 삭제합니다.")
    public ResponseEntity<Void> deleteFcmToken();
    
    @Operation(summary = "알림 목록 확인", description = "알림을 확인합니다.")
    public ResponseEntity<List<AlarmResponse>> getAlarmList();
    
    @Operation(summary = "알림 실행", description = "알림을 확인 상태로 만듭니다.")
    public ResponseEntity<Void> processOneAlarm(@PathVariable Long id);
    
    @Operation(summary = "알림 삭제", description = "더 이상 보고싶지 않은 알림을 삭제합니다.")
    public ResponseEntity<Void> deleteAlarm(@PathVariable Long id);
}
