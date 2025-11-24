package my.inplace.application.alarm.query;

import lombok.RequiredArgsConstructor;
import my.inplace.domain.alarm.Alarm;
import my.inplace.infra.alarm.jpa.AlarmJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmQueryService {

    private final AlarmJpaRepository alarmJpaRepository;

    @Transactional(readOnly = true)
    public List<Alarm> getAlarmInfos(Long userId) {
        return alarmJpaRepository.findByUserId(userId);
    }
    
    @Transactional
    public void deleteAlarm(Long alarmId) {
        alarmJpaRepository.deleteById(alarmId);
    }
}
