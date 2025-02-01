package team7.inplace.admin.crawling.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.admin.crawling.application.CrawlingFacade;
import team7.inplace.admin.crawling.presentation.dto.PlaceRegistrationRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crawling")
@PreAuthorize("hasRole('ADMIN')")
public class CrawlingController {

    private final CrawlingFacade crawlingFacade;

    @PostMapping("/video/place/register")
    public ResponseEntity<Void> addPlaceInfo(
        @RequestBody PlaceRegistrationRequest placeRegistrationRequest
    ) {
        crawlingFacade.addPlaceInfo(placeRegistrationRequest.toCommand());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

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
