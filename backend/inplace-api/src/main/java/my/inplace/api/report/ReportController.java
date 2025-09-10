package my.inplace.api.report;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import my.inplace.application.report.command.ReportCommandFacade;
import my.inplace.api.report.dto.ReportRequest.SubmitReport;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController implements ReportControllerApiSpec {

    private final ReportCommandFacade reportCommandFacade;

    @Override
    @PostMapping("/post")
    public ResponseEntity<Void> submitPostReport(@RequestBody SubmitReport request) {
        reportCommandFacade.processPostReport(request.id());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PostMapping("/comment")
    public ResponseEntity<Void> submitCommentReport(@RequestBody SubmitReport request) {
        reportCommandFacade.processCommentReport(request.id());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
