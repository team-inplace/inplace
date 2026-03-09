package my.inplace.infra.alarm.dto;

public class AlarmData {

    public record Comment(
        Long postId,
        Long commentId
    ) {

    }
}
