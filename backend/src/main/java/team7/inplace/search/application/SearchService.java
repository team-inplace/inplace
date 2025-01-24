package team7.inplace.search.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.influencer.persistence.dto.InfluencerQueryResult;
import team7.inplace.search.application.dto.AutoCompletionInfo;
import team7.inplace.search.application.dto.SearchType;
import team7.inplace.search.persistence.InfluencerSearchRepository;
import team7.inplace.search.persistence.PlaceSearchRepository;
import team7.inplace.search.persistence.VideoSearchRepository;
import team7.inplace.search.persistence.dto.SearchQueryResult;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.video.persistence.dto.VideoQueryResult;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {

    private static final Integer MAX_COMPLETION_RESULTS = 5;
    private static final Integer SEARCH_LIMIT = 10;

    private final VideoSearchRepository videoSearchRepository;
    private final InfluencerSearchRepository influencerSearchRepository;
    private final PlaceSearchRepository placeSearchRepository;

    public List<AutoCompletionInfo> searchAutoCompletions(String keyword) {
        var influencerSearchInfo = influencerSearchRepository.searchAutoComplete(keyword);
        var placeSearchInfo = placeSearchRepository.searchAutoComplete(keyword);

        var influencerAutoComplete = influencerSearchInfo.stream()
            .map(
                info -> new AutoCompletionInfo(info.keyword(), info.score(), SearchType.INFLUENCER))
            .toList();
        var placeAutoComplete = placeSearchInfo.stream()
            .map(info -> new AutoCompletionInfo(info.keyword(), info.score(), SearchType.PLACE))
            .toList();

        return Stream.concat(influencerAutoComplete.stream(), placeAutoComplete.stream())
            .sorted(Comparator.comparing(AutoCompletionInfo::score).reversed())
            .limit(MAX_COMPLETION_RESULTS)
            .toList();
    }

    public Page<InfluencerQueryResult.Simple> searchInfluencer(String keyword, Pageable pageable) {
        var userId = AuthorizationUtil.getUserId();
        var influencerInfos = influencerSearchRepository.search(keyword, pageable, userId);

        return influencerInfos;
    }

    public List<InfluencerQueryResult.Simple> searchInfluencer(String keyword) {
        var userId = AuthorizationUtil.getUserId();
        var influencerInfos = influencerSearchRepository.search(
            keyword,
            Pageable.ofSize(SEARCH_LIMIT),
            userId
        );

        return influencerInfos.getContent();
    }

    public List<VideoQueryResult.SimpleVideo> searchVideo(String keyword) {
        var userId = AuthorizationUtil.getUserId();
        var videoInfos = videoSearchRepository.search(
            keyword,
            Pageable.ofSize(SEARCH_LIMIT),
            userId
        );

        return videoInfos.getContent();
    }

    public List<SearchQueryResult.Place> searchPlace(String keyword) {
        var userId = AuthorizationUtil.getUserId();
        var placeInfos = placeSearchRepository.search(keyword,
            Pageable.ofSize(SEARCH_LIMIT),
            userId
        );

        return placeInfos.getContent();
    }
}
