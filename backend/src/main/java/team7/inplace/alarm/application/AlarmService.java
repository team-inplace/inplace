package team7.inplace.alarm.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.alarm.application.dto.AlarmInfo;
import team7.inplace.alarm.domain.Alarm;
import team7.inplace.alarm.domain.AlarmType;
import team7.inplace.alarm.persistent.AlarmRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    
    @Transactional
    public void saveAlarm(Long userId, Long postId, Long commentId, String content, AlarmType alarmType) {
        Alarm alarm = new Alarm(userId, postId, commentId, content, alarmType);
        
        alarmRepository.save(alarm);
    }
    
    @Transactional(readOnly = true)
    public List<AlarmInfo> getAlarmInfos(Long userId) {
        return alarmRepository.findByUserId(userId).stream()
            .map(AlarmInfo::from)
            .toList();
    }
    
    @Transactional
    public void readAlarm(Long id) {
        Alarm alarm = alarmRepository.findById(id).orElseThrow();
        alarm.checked();
    }
}
