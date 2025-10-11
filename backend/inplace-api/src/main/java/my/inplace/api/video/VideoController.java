package my.inplace.api.video;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import my.inplace.application.video.command.VideoCommandFacade;
import my.inplace.application.video.command.VideoCommandService;
import my.inplace.api.video.dto.VideoResponse;
import my.inplace.api.video.dto.VideoResponse.Detail;
import my.inplace.api.video.dto.VideoResponse.Simple;
import my.inplace.application.video.query.VideoQueryFacade;
import my.inplace.application.video.query.VideoQueryService;
import my.inplace.application.video.query.dto.VideoParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoController implements VideoControllerApiSpec {

    private final VideoQueryFacade videoQueryFacade;
    private final VideoCommandFacade videoCommandFacade;
    private final VideoQueryService videoQueryService;
    private final VideoCommandService videoCommandService;

    @Override
    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Detail>> readVideos(
        @RequestParam(value = "longitude", defaultValue = "128.6") String longitude,
        @RequestParam(value = "latitude", defaultValue = "35.9") String latitude,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var searchParams = VideoParam.SquareBound.from(longitude, latitude);
        var videoResponses = videoQueryService.getVideosBySurround(searchParams, pageable, true)
            .stream().map(VideoResponse.Detail::from).toList();
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @Override
    @GetMapping("/new")
    public ResponseEntity<List<VideoResponse.Detail>> readByNew() {
        var videoResponses = videoQueryService.getRecentVideos()
            .stream().map(VideoResponse.Detail::from).toList();
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @Override
    @GetMapping("/cool/{category}")
    public ResponseEntity<List<VideoResponse.Detail>> readByCool(@PathVariable String category) {
        var videoResponses = videoQueryService.getCoolVideo(category)
            .stream().map(VideoResponse.Detail::from).toList();
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @Override
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<VideoResponse.Detail>> readByMyInfluencer() {
        var myVideos = videoQueryFacade.getMyInfluencerVideos()
            .stream().map(VideoResponse.Detail::from).toList();
        return new ResponseEntity<>(myVideos, HttpStatus.OK);
    }

    @Override
    @GetMapping("/null")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Simple>> readPlaceNullVideo(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var videoResponses = videoQueryFacade.getVideoWithNoPlace(pageable)
            .map(VideoResponse.Simple::from);
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{videoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long videoId) {
        videoCommandService.deleteVideo(videoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @GetMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateMainVideos() {
        videoCommandFacade.updateCoolVideos();
        videoCommandService.updateRecentVideos();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
