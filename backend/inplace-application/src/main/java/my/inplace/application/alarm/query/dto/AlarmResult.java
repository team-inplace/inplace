package my.inplace.application.alarm.query.dto;

import my.inplace.application.alarm.util.TimeUtil;
import my.inplace.domain.alarm.query.AlarmQueryResult;

public record AlarmResult(
    Long alarmId,
    Long postId,
    Long commentId,
    int pageNumber,
    String content,
    Boolean checked,
    String type,
    String createdAt,
    Boolean postDeleted,
    Boolean commentDeleted
) {

    public static AlarmResult from(AlarmQueryResult.Detail alarm) {
        var postDeleted = alarm.postDeleted();
        var commentDeleted = alarm.commentDeleted();
        var postId = postDeleted ? null : alarm.postId();
        var commentId = (postDeleted || commentDeleted) ? null : alarm.commentId();

        return new AlarmResult(
            alarm.id(),
            postId,
            commentId,
            alarm.pageNumber().intValue() / 10,
            alarm.content(),
            alarm.checked(),
            alarm.alarmType().name(),
            TimeUtil.betweenTime(alarm.createdAt()),
            postDeleted,
            commentDeleted
        );
    }

}
