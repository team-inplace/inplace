package my.inplace.infra.alarm.jpa;

import my.inplace.domain.alarm.AlarmOutBox;
import my.inplace.domain.alarm.AlarmStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmOutBoxJpaRepository extends JpaRepository<AlarmOutBox, Long> {
    List<AlarmOutBox> findAllByAlarmStatus(AlarmStatus alarmStatus);
    
    List<AlarmOutBox> findAllByReceiverId(Long receiverId);
}
