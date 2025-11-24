package my.inplace.application.alarm.command;

import lombok.RequiredArgsConstructor;
import my.inplace.domain.alarm.Alarm;
import my.inplace.domain.alarm.AlarmOutBox;
import my.inplace.domain.alarm.AlarmType;
import my.inplace.infra.alarm.jpa.AlarmJpaRepository;
import my.inplace.infra.alarm.jpa.AlarmOutBoxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmCommandService {
    
    private final AlarmJpaRepository alarmJpaRepository;
    private final AlarmOutBoxRepository alarmOutBoxRepository;

    @Transactional
    public void checkAlarm(Long id) {
        Alarm alarm = alarmJpaRepository.findById(id).orElseThrow();
        alarm.checked();
    }

    @Transactional
    public void saveAlarm(
        Long userId, Long postId, Long commentId, String content, AlarmType alarmType) {
        Alarm alarm = new Alarm(userId, postId, commentId, content, alarmType);

        alarmJpaRepository.save(alarm);
    }
    
    @Transactional
    public void saveAlarmEvent(
        Long receiverId, String title, String content
    ) {
        AlarmOutBox alarmEvent = new AlarmOutBox(receiverId, title, content);
        
        alarmOutBoxRepository.save(alarmEvent);
    }
}
