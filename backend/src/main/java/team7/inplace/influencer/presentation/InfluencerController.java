package team7.inplace.influencer.presentation;

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
import team7.inplace.influencer.application.InfluencerService;
import team7.inplace.influencer.application.dto.InfluencerCommand;
import team7.inplace.influencer.presentation.dto.InfluencerRequest;
import team7.inplace.influencer.presentation.dto.InfluencerRequest.Like;
import team7.inplace.influencer.presentation.dto.InfluencerRequest.Upsert;
import team7.inplace.influencer.presentation.dto.InfluencerResponse;
import team7.inplace.influencer.presentation.dto.InfluencerResponse.Detail;

@RequiredArgsConstructor
@RestController
@RequestMapping("/influencers")
public class InfluencerController implements InfluencerControllerApiSpec {

    private final InfluencerService influencerService;

    @Override
    @GetMapping()
    public ResponseEntity<Page<InfluencerResponse.Info>> getAllInfluencers(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var influencers = influencerService.getAllInfluencers(pageable)
            .map(InfluencerResponse.Info::from);

        return new ResponseEntity<>(influencers, HttpStatus.OK);
    }

    @Override
    @GetMapping("/names")
    public ResponseEntity<List<InfluencerResponse.Name>> getAllInfluencerNames() {
        var names = influencerService.getAllInfluencerNames().stream()
            .map(InfluencerResponse.Name::from)
            .toList();
        return new ResponseEntity<>(names, HttpStatus.OK);
    }

    @Override
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> createInfluencer(@RequestBody Upsert request) {
        var command = request.toCommand();
        Long savedId = influencerService.createInfluencer(command);

        return new ResponseEntity<>(savedId, HttpStatus.OK);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> updateInfluencer(
        @PathVariable Long id,
        @RequestBody Upsert request
    ) {
        InfluencerCommand influencerCommand = request.toCommand();
        Long updatedId = influencerService.updateInfluencer(id, influencerCommand);

        return new ResponseEntity<>(updatedId, HttpStatus.OK);
    }

    @PatchMapping("/{id}/visibility")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> updateVisibility(@PathVariable Long id) {
        Long updatedId = influencerService.updateVisibility(id);

        return new ResponseEntity<>(updatedId, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> deleteInfluencer(@PathVariable Long id) {
        influencerService.deleteInfluencer(id);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @Override
    @PostMapping("/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addLikeInfluencer(@RequestBody Like request) {
        var command = request.toCommand();
        influencerService.likeToInfluencer(command);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PostMapping("/multiple/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addLikeInfluencers(@RequestBody InfluencerRequest.Likes request) {
        var command = request.toCommand();
        influencerService.likeToManyInfluencer(command);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Detail> getInfluencer(@PathVariable Long id) {
        var influencer = influencerService.getInfluencerDetail(id);

        var response = InfluencerResponse.Detail.from(influencer);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
