package my.inplace.infra.alarm.jpa;

import java.util.List;

import my.inplace.domain.alarm.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmJpaRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findByUserId(Long userId);
}
