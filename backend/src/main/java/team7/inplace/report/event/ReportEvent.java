package team7.inplace.report.event;

public class ReportEvent {

    public record PostReportEvent(Long postId) {}

    public record CommentReportEvent(Long commentId) {}

}
