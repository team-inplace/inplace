package my.inplace.application.alarm.event.dto;

import jakarta.annotation.Nullable;
import my.inplace.domain.alarm.AlarmOutBox;

import java.util.UUID;

public record AlarmEvent(
    UUID idempotencyKey,
    String title,
    String body,
    @Nullable String fcmToken,
    @Nullable String expoToken
) {
    public static AlarmEvent from(AlarmOutBox alarmEvent) {
        return new AlarmEvent(
            alarmEvent.getIdempotencyKey(),
            alarmEvent.getTitle(),
            alarmEvent.getContent(),
            alarmEvent.getFcmToken(),
            alarmEvent.getExpoToken()
        );
    }
}
