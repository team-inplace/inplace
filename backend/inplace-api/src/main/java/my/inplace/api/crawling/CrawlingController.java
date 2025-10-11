package my.inplace.api.crawling;

import lombok.RequiredArgsConstructor;
import my.inplace.application.crawling.CrawlingFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crawling")
@PreAuthorize("hasRole('ADMIN')")
public class CrawlingController {

    private final CrawlingFacade crawlingFacade;

    @PostMapping("/video")
    public ResponseEntity<Void> crawlingVideo() {
        crawlingFacade.updateVideos();

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/video/view")
    public ResponseEntity<Void> crawlingVideoView() {
        crawlingFacade.updateVideoView();

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
