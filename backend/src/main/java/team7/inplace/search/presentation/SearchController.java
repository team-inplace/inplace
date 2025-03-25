package team7.inplace.search.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.influencer.presentation.dto.InfluencerResponse.Info;
import team7.inplace.search.application.SearchService;
import team7.inplace.search.application.dto.AutoCompletionInfo;
import team7.inplace.search.presentation.dto.SearchRequest;
import team7.inplace.search.presentation.dto.SearchResponse;
import team7.inplace.video.presentation.dto.VideoResponse.Simple;

@RestController
@RequestMapping("/search")
@Slf4j
@RequiredArgsConstructor
public class SearchController implements SearchControllerApiSpec {

    private final SearchService searchService;

    @Override
    @GetMapping("/complete")
    public ResponseEntity<List<AutoCompletionInfo>> searchKeywords(
        @ModelAttribute SearchRequest.AutoComplete request
    ) {
        log.info("searchKeywords: {}", request);
        var autoCompletions = searchService.searchAutoCompletions(
            request.getType(),
            request.getValue()
        );

        return new ResponseEntity<>(autoCompletions, HttpStatus.OK);
    }

    @Override
    @GetMapping("/video")
    public ResponseEntity<List<Simple>> searchVideo(@RequestParam(name = "value") String keyword) {
        var videos = searchService.searchVideo(keyword);

        var response = videos.stream()
            .map(Simple::from)
            .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/influencer")
    public ResponseEntity<List<Info>> getInfluencersForPaging(
        @RequestParam(name = "value") String keyword
    ) {
        var influencers = searchService.searchInfluencer(keyword);

        var response = influencers.stream()
            .map(Info::from)
            .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/page/influencer")
    public ResponseEntity<Page<Info>> getInfluencersForPaging(
        @RequestParam(name = "value") String keyword, Pageable pageable) {
        var influencers = searchService.searchInfluencer(keyword, pageable);

        var response = influencers.map(Info::from);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/place")
    public ResponseEntity<List<SearchResponse.Place>> searchPlace(String keyword) {
        var places = searchService.searchPlace(keyword);

        var response = places.stream()
            .map(SearchResponse.Place::from)
            .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
