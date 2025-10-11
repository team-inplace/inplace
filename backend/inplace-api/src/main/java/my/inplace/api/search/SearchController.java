package my.inplace.api.search;

import my.inplace.api.influencer.dto.InfluencerResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import my.inplace.api.search.dto.SearchRequest;
import my.inplace.api.search.dto.SearchResponse;
import my.inplace.application.search.query.SearchQueryService;
import my.inplace.api.video.dto.VideoResponse;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController implements SearchControllerApiSpec {

    private final SearchQueryService searchQueryService;

    @Override
    @GetMapping("/complete")
    public ResponseEntity<List<SearchResponse.AutoCompletion>> searchKeywords(
        @ModelAttribute SearchRequest.AutoComplete request
    ) {
        var autoCompletions = searchQueryService.searchAutoCompletions(
            request.getType(),
            request.getValue()
        );

        var response = autoCompletions.stream()
            .map(SearchResponse.AutoCompletion::from)
            .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/video")
    public ResponseEntity<List<VideoResponse.Detail>> searchVideo(@RequestParam(name = "value") String keyword) {
        var videos = searchQueryService.searchVideo(keyword);

        var response = videos.stream()
            .map(VideoResponse.Detail::from)
            .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/influencer")
    public ResponseEntity<List<InfluencerResponse.Simple>> getInfluencersForPaging(
        @RequestParam(name = "value") String keyword
    ) {
        var influencers = searchQueryService.searchInfluencer(keyword);

        var response = influencers.stream()
            .map(InfluencerResponse.Simple::from)
            .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/page/influencer")
    public ResponseEntity<Page<InfluencerResponse.Simple>> getInfluencersForPaging(
        @RequestParam(name = "value") String keyword, Pageable pageable) {
        var influencers = searchQueryService.searchInfluencer(keyword, pageable);

        var response = influencers.map(InfluencerResponse.Simple::from);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/place")
    public ResponseEntity<List<SearchResponse.Place>> searchPlace(String keyword) {
        var places = searchQueryService.searchPlace(keyword);

        var response = places.stream()
            .map(SearchResponse.Place::from)
            .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
