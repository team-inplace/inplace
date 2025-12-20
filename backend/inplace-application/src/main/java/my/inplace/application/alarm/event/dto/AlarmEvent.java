package my.inplace.application.alarm.event.dto;

import my.inplace.domain.alarm.AlarmOutBox;

public record AlarmEvent(
    Long id
) {
    public static AlarmEvent from(AlarmOutBox alarmEvent) {
        return new AlarmEvent(
            alarmEvent.getId()
        );
    }
}
