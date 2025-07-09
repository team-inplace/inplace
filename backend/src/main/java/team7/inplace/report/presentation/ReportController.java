package team7.inplace.report.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.report.application.ReportFacade;
import team7.inplace.report.presentation.dto.ReportRequest.SubmitReport;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController implements ReportControllerApiSpec {

    private final ReportFacade reportFacade;

    @Override
    @PostMapping("/post")
    public ResponseEntity<Void> submitPostReport(@RequestBody SubmitReport request) {
        reportFacade.processPostReport(request.id());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PostMapping("/comment")
    public ResponseEntity<Void> submitCommentReport(@RequestBody SubmitReport request) {
        reportFacade.processCommentReport(request.id());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
