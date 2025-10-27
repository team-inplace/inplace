package my.inplace.application.search.query;

import my.inplace.application.influencer.query.dto.InfluencerResult;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.search.SearchRepository;
import my.inplace.domain.video.query.VideoQueryResult.DetailedVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.infra.search.InfluencerSearchQueryDslRepository;
import my.inplace.infra.search.PlaceSearchQueryDslRepository;
import my.inplace.infra.search.VideoSearchQueryDslRepository;
import my.inplace.application.search.query.dto.SearchType;
import my.inplace.application.search.query.dto.SearchResult;
import my.inplace.security.util.AuthorizationUtil;
import my.inplace.application.video.query.dto.VideoResult;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchQueryService {
    private static final Integer MAX_COMPLETION_RESULTS = 5;
    private static final Integer SEARCH_LIMIT = 10;

    private final SearchRepository<DetailedVideo> videoSearchRepository;
    private final InfluencerSearchQueryDslRepository influencerSearchRepository;
    private final PlaceSearchQueryDslRepository placeSearchRepository;

    public List<SearchResult.AutoCompletion> searchAutoCompletions(SearchType type, String keyword) {
        var placeSearchInfo = placeSearchRepository.searchAutoComplete(keyword).stream()
            .map(
                info -> new SearchResult.AutoCompletion(info.keyword(), info.score(), SearchType.PLACE))
            .toList();

        if (type.equals(SearchType.PLACE)) {
            return placeSearchInfo;
        }

        var influencerSearchInfo = influencerSearchRepository.searchAutoComplete(keyword).stream()
            .map(
                info -> new SearchResult.AutoCompletion(info.keyword(), info.score(), SearchType.INFLUENCER))
            .toList();

        return Stream.concat(influencerSearchInfo.stream(), placeSearchInfo.stream())
            .sorted(Comparator.comparing(SearchResult.AutoCompletion::score).reversed())
            .limit(MAX_COMPLETION_RESULTS)
            .toList();
    }

    public Page<InfluencerResult.Simple> searchInfluencer(String keyword, Pageable pageable) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var influencerInfos = influencerSearchRepository.search(keyword, pageable, userId);

        return influencerInfos.map(InfluencerResult.Simple::from);
    }

    public List<InfluencerResult.Simple> searchInfluencer(String keyword) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var influencerInfos = influencerSearchRepository.search(
            keyword,
            Pageable.ofSize(SEARCH_LIMIT),
            userId
        );

        return influencerInfos.getContent().stream()
            .map(InfluencerResult.Simple::from)
            .toList();
    }

    public List<VideoResult.DetailedVideo> searchVideo(String keyword) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var videoInfos = videoSearchRepository.search(
            keyword,
            Pageable.ofSize(SEARCH_LIMIT),
            userId
        );

        return videoInfos.getContent().stream()
            .map(VideoResult.DetailedVideo::from)
            .toList();
    }

    public List<SearchResult.Place> searchPlace(String keyword) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var placeInfos = placeSearchRepository.search(
            keyword,
            Pageable.ofSize(SEARCH_LIMIT),
            userId
        );

        return placeInfos.getContent().stream()
            .map(SearchResult.Place::from)
            .toList();
    }
}
