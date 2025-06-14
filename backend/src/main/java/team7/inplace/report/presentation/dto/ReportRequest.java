package team7.inplace.report.presentation.dto;

public class ReportRequest {

    public record SubmitReport (
        Long id,
        String reason
    ) {

    }

}
