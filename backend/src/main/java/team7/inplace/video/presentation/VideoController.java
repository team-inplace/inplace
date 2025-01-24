package team7.inplace.video.presentation;

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
import team7.inplace.video.application.VideoFacade;
import team7.inplace.video.application.VideoService;
import team7.inplace.video.presentation.dto.VideoResponse;
import team7.inplace.video.presentation.dto.VideoSearchParams;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoController implements VideoControllerApiSpec {
    private final VideoService videoService;
    private final VideoFacade videoFacade;

    @Override
    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public ResponseEntity<List<VideoResponse.Simple>> readVideos(
            @RequestParam(value = "longitude", defaultValue = "128.6") String longitude,
            @RequestParam(value = "latitude", defaultValue = "35.9") String latitude,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        VideoSearchParams searchParams = VideoSearchParams.from(longitude, latitude);
        var videoResponses = videoService.getVideosBySurround(searchParams, pageable)
                .stream().map(VideoResponse.Simple::from).toList();
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @Override
    @GetMapping("/new")
    public ResponseEntity<List<VideoResponse.Simple>> readByNew() {
        var videoResponses = videoService.getAllVideosDesc()
                .stream().map(VideoResponse.Simple::from).toList();
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @Override
    @GetMapping("/cool")
    public ResponseEntity<List<VideoResponse.Simple>> readByCool() {
        var videoResponses = videoService.getCoolVideo()
                .stream().map(VideoResponse.Simple::from).toList();
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my")
    public ResponseEntity<List<VideoResponse.Simple>> readByMyInfluencer() {
        var myVideos = videoFacade.getMyInfluencerVideos()
                .stream().map(VideoResponse.Simple::from).toList();
        return new ResponseEntity<>(myVideos, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{influencerId}")
    public ResponseEntity<Page<VideoResponse.Simple>> readByOneInfluencer(
            @PageableDefault(page = 0, size = 6) Pageable pageable,
            @PathVariable Long influencerId
    ) {
        var videoResponses = videoService.getOneInfluencerVideos(influencerId, pageable)
                .map(VideoResponse.Simple::from);
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @Override
    @GetMapping("/null")
    public ResponseEntity<Page<VideoResponse.Simple>> readPlaceNullVideo(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var videoResponses = videoFacade.getVideoWithNoPlace(pageable)
                .map(VideoResponse.Simple::from);
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @Override
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long videoId) {
        videoService.deleteVideo(videoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
