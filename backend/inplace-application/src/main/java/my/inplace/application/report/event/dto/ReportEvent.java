package my.inplace.application.report.event.dto;

public class ReportEvent {

    public record PostReportEvent(Long postId) {}

    public record CommentReportEvent(Long commentId) {}

}
