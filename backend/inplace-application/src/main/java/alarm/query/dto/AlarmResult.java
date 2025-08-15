package alarm.query.dto;

import alarm.Alarm;
import alarm.util.TimeUtil;

public record AlarmResult(
    Long alarmId,
    Long postId,
    Long commentId,
    String content,
    Boolean checked,
    String type,
    String createdAt
) {

    public static AlarmResult from(Alarm alarm) { // TODO
        return new AlarmResult(
            alarm.getId(),
            alarm.getPostId(),
            alarm.getCommentId(),
            alarm.getContent(),
            alarm.isChecked(),
            alarm.getAlarmType().name(),
            TimeUtil.betweenTime(alarm.getCreatedAt())
        );
    }
}
