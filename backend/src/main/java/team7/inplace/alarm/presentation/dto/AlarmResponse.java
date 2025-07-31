package team7.inplace.alarm.presentation.dto;

import team7.inplace.alarm.application.dto.AlarmInfo;

public record AlarmResponse(
    Long alarmId,
    Long postId,
    Long commentId,
    String content,
    Boolean checked,
    String type,
    String createdAt
) {
    public static AlarmResponse from(AlarmInfo alarmInfo) {
        return new AlarmResponse(
            alarmInfo.alarmId(),
            alarmInfo.postId(),
            alarmInfo.commentId(),
            alarmInfo.content(),
            alarmInfo.checked(),
            alarmInfo.type(),
            alarmInfo.createdAt()
        );
    }
}
