package my.inplace.api.report.dto;

public class ReportRequest {

    public record SubmitReport(
        Long id,
        String reason
    ) {

    }

}
