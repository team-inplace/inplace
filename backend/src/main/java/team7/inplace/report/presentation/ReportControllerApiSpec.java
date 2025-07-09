package team7.inplace.report.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;
import team7.inplace.report.presentation.dto.ReportRequest.SubmitReport;


public interface ReportControllerApiSpec {

    @Operation(summary = "게시글 신고 요청", description = "게시글 신고 요청을 처리합니다.")
    ResponseEntity<Void> submitPostReport(@RequestBody SubmitReport request);

    @Operation(summary = "댓글 신고 요청", description = "댓글 신고 요청을 처리합니다.")
    ResponseEntity<Void> submitCommentReport(@RequestBody SubmitReport request);

}
