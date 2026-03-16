package my.inplace.api.alarm.dto;

import my.inplace.application.alarm.query.dto.AlarmResult;

public record AlarmResponse(
    Long alarmId,
    Long postId,
    Long commentId,
    int commentPage,
    String content,
    Boolean checked,
    String type,
    String createdAt,
    Boolean postDeleted,
    Boolean commentDeleted
) {

    public static AlarmResponse from(AlarmResult alarmResult) {
        return new AlarmResponse(
            alarmResult.alarmId(),
            alarmResult.postId(),
            alarmResult.commentId(),
            alarmResult.pageNumber(),
            alarmResult.content(),
            alarmResult.checked(),
            alarmResult.type(),
            alarmResult.createdAt(),
            alarmResult.postDeleted(),
            alarmResult.commentDeleted()
        );
    }
}
