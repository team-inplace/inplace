package my.inplace.application.alarm.command;

import my.inplace.domain.alarm.Alarm;
import my.inplace.domain.alarm.AlarmType;
import my.inplace.infra.alarm.jpa.AlarmJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmCommandService {

    private final AlarmJpaRepository alarmJpaRepository;

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
}
