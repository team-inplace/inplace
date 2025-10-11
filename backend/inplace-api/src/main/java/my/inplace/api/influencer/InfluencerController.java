package my.inplace.api.influencer;

import my.inplace.application.influencer.command.InfluencerCommandService;
import my.inplace.api.influencer.dto.InfluencerRequest;
import my.inplace.api.influencer.dto.InfluencerRequest.Like;
import my.inplace.api.influencer.dto.InfluencerRequest.Upsert;
import my.inplace.api.influencer.dto.InfluencerResponse;
import my.inplace.api.influencer.dto.InfluencerResponse.Detail;
import my.inplace.api.influencer.dto.InfluencerResponse.Video;
import my.inplace.application.influencer.query.InfluencerQueryService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import my.inplace.application.video.query.VideoQueryService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/influencers")
public class InfluencerController implements InfluencerControllerApiSpec {

    private final InfluencerQueryService influencerQueryService;
    private final InfluencerCommandService influencerCommandService;
    private final VideoQueryService videoQueryService;

    @Override
    @GetMapping()
    public ResponseEntity<Page<InfluencerResponse.Simple>> getAllInfluencers(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var influencers = influencerQueryService.getAllInfluencers(pageable)
            .map(InfluencerResponse.Simple::from);

        return new ResponseEntity<>(influencers, HttpStatus.OK);
    }

    @Override
    @GetMapping("/names")
    public ResponseEntity<List<InfluencerResponse.Name>> getAllInfluencerNames() {
        var names = influencerQueryService.getAllInfluencerNames().stream()
            .map(InfluencerResponse.Name::from)
            .toList();
        return new ResponseEntity<>(names, HttpStatus.OK);
    }

    @Override
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> createInfluencer(@RequestBody Upsert request) {
        var command = request.toCommand();
        Long savedId = influencerCommandService.createInfluencer(command);

        return new ResponseEntity<>(savedId, HttpStatus.OK);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> updateInfluencer(
        @PathVariable Long id,
        @RequestBody Upsert request
    ) {
        var influencerCommand = request.toCommand(id);
        Long updatedId = influencerCommandService.updateInfluencer(influencerCommand);

        return new ResponseEntity<>(updatedId, HttpStatus.OK);
    }

    @PatchMapping("/{id}/visibility")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> updateVisibility(@PathVariable Long id) {
        Long updatedId = influencerCommandService.updateVisibility(id);

        return new ResponseEntity<>(updatedId, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> deleteInfluencer(@PathVariable Long id) {
        influencerCommandService.deleteInfluencer(id);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @Override
    @PostMapping("/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addLikeInfluencer(@RequestBody Like request) {
        var command = request.toCommand();
        influencerCommandService.likeToInfluencer(command);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PostMapping("/multiple/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addLikeInfluencers(@RequestBody InfluencerRequest.Likes request) {
        var command = request.toCommand();
        influencerCommandService.likeToManyInfluencer(command);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Detail> getInfluencer(@PathVariable Long id) {
        var influencer = influencerQueryService.getInfluencerDetail(id);

        var response = InfluencerResponse.Detail.from(influencer);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{influencerId}/videos")
    public ResponseEntity<Page<Video>> getInfluencerVideos(
        Pageable pageable,
        @PathVariable Long influencerId
    ) {
        var videos = videoQueryService.getOneInfluencerVideos(influencerId, pageable)
            .map(InfluencerResponse.Video::from);

        return new ResponseEntity<>(videos, HttpStatus.OK);
    }
}
