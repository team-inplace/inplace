package my.inplace.application.alarm.event;

import lombok.RequiredArgsConstructor;
import my.inplace.application.alarm.event.dto.AlarmEvent;
import my.inplace.domain.alarm.AlarmOutBox;
import my.inplace.domain.alarm.AlarmStatus;
import my.inplace.infra.alarm.jpa.AlarmOutBoxRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlarmEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    private final AlarmOutBoxRepository alarmOutBoxRepository;
    
    @Scheduled(cron = "0 0/15 * * * *")
    public void publishOutBoxEvent() {
        List<AlarmOutBox> alarmEvents = alarmOutBoxRepository.findAllByAlarmStatus(AlarmStatus.PENDING);
        
        for (AlarmOutBox alarmEvent : alarmEvents) {
            eventPublisher.publishEvent(AlarmEvent.from(alarmEvent));
        }
    }
}
