package my.inplace.infra.alarm.jpa;

import my.inplace.domain.alarm.AlarmOutBox;
import my.inplace.domain.alarm.AlarmStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlarmOutBoxRepository extends JpaRepository<AlarmOutBox, UUID> {
    List<AlarmOutBox> findAllByAlarmStatus(AlarmStatus alarmStatus);
}
