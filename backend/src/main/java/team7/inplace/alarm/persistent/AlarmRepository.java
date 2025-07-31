package team7.inplace.alarm.persistent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team7.inplace.alarm.domain.Alarm;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByUserId(Long userId);
}
