package my.inplace.application.alarm.event.dto;

import my.inplace.domain.alarm.AlarmType;

import jakarta.annotation.Nullable;

public record AlarmEvent(
    Long postId,
    Long commentId,
    @Nullable String sender,
    @Nullable String receiver,
    AlarmType alarmType
) {
}
