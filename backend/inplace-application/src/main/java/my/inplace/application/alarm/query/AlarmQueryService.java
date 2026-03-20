package my.inplace.application.alarm.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import my.inplace.application.alarm.query.dto.AlarmResult;
import my.inplace.domain.alarm.AlarmOutBox;
import my.inplace.domain.alarm.AlarmStatus;
import my.inplace.domain.alarm.query.AlarmReadRepository;
import my.inplace.infra.alarm.jpa.AlarmJpaRepository;
import my.inplace.infra.alarm.jpa.AlarmOutBoxJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmQueryService {

    private final AlarmJpaRepository alarmJpaRepository;
    private final AlarmOutBoxJpaRepository alarmOutBoxJpaRepository;
    private final AlarmReadRepository alarmReadRepository;

    @Transactional(readOnly = true)
    public List<AlarmResult> getAlarms(Long userId) {
        var alarms = alarmReadRepository.findAlarms(userId);
        return alarms.stream().map(AlarmResult::from).toList();
    }
    
    @Transactional(readOnly = true)
    public List<AlarmOutBox> getAlarmEventByReceiverIdAndAlarmStatus(Long receiverId, AlarmStatus alarmStatus) {
        return alarmOutBoxJpaRepository.findAllByReceiverIdAndAlarmStatus(receiverId, alarmStatus);
    }
    
    @Transactional
    public void deleteAlarm(Long alarmId) {
        alarmJpaRepository.deleteById(alarmId);
    }
}
