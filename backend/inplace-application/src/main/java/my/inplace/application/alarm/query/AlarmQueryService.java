package my.inplace.application.alarm.query;

import lombok.RequiredArgsConstructor;
import my.inplace.domain.alarm.Alarm;
import my.inplace.domain.alarm.AlarmOutBox;
import my.inplace.infra.alarm.jpa.AlarmJpaRepository;
import my.inplace.infra.alarm.jpa.AlarmOutBoxJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmQueryService {

    private final AlarmJpaRepository alarmJpaRepository;
    private final AlarmOutBoxJpaRepository alarmOutBoxJpaRepository;

    @Transactional(readOnly = true)
    public List<Alarm> getAlarmInfos(Long userId) {
        return alarmJpaRepository.findByUserId(userId);
    }
    
    @Transactional(readOnly = true)
    public List<AlarmOutBox> getAlarmEventByReceiverId(Long receiverId) {
        return alarmOutBoxJpaRepository.findAllByReceiverId(receiverId);
    }
    
    @Transactional
    public void deleteAlarm(Long alarmId) {
        alarmJpaRepository.deleteById(alarmId);
    }
}
