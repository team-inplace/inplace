package my.inplace.domain.alarm.query;

import java.time.LocalDateTime;
import my.inplace.domain.alarm.AlarmType;

public class AlarmQueryResult {

    public record Detail(
        Long id,
        Long postId,
        Long commentId,
        Long pageNumber,
        String content,
        boolean checked,
        AlarmType alarmType,
        LocalDateTime createdAt,
        boolean postDeleted,
        boolean commentDeleted
    ) {

    }
}
