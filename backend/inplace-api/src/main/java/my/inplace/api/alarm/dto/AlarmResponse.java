package my.inplace.api.alarm.dto;

import my.inplace.application.alarm.query.dto.AlarmResult;

public record AlarmResponse(
    Long alarmId,
    Long postId,
    Long commentId,
    int pageNumber,
    int offset,
    String content,
    Boolean checked,
    String type,
    String createdAt
) {

    public static AlarmResponse from(AlarmResult alarmResult) {
        return new AlarmResponse(
            alarmResult.alarmId(),
            alarmResult.postId(),
            alarmResult.commentId(),
            alarmResult.pageNumber(),
            alarmResult.offset(),
            alarmResult.content(),
            alarmResult.checked(),
            alarmResult.type(),
            alarmResult.createdAt()
        );
    }
}
