package my.inplace.application.alarm.event;

import lombok.RequiredArgsConstructor;
import my.inplace.application.alarm.event.dto.AlarmEvent;
import my.inplace.domain.alarm.AlarmOutBox;
import my.inplace.domain.alarm.AlarmStatus;
import my.inplace.infra.alarm.jpa.AlarmOutBoxJpaRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlarmEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    private final AlarmOutBoxJpaRepository alarmOutBoxJpaRepository;
    
    @Scheduled(cron = "0 0/15 * * * *")
    public void publishOutBoxEvent() {
        List<AlarmOutBox> alarmEvents = alarmOutBoxJpaRepository.findAllByAlarmStatus(AlarmStatus.READY);
        
        for (AlarmOutBox alarmEvent : alarmEvents) {
            eventPublisher.publishEvent(AlarmEvent.from(alarmEvent));
        }
    }
}
