package team7.inplace.search.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.liked.likedInfluencer.persistent.FavoriteInfluencerRepository;
import team7.inplace.influencer.application.dto.InfluencerInfo;
import team7.inplace.influencer.domain.Influencer;
import team7.inplace.liked.likedPlace.persistence.LikedPlaceRepository;
import team7.inplace.search.application.dto.AutoCompletionInfo;
import team7.inplace.search.application.dto.PlaceSearchInfo;
import team7.inplace.search.application.dto.SearchType;
import team7.inplace.search.persistence.InfluencerSearchRepository;
import team7.inplace.search.persistence.PlaceSearchRepository;
import team7.inplace.search.persistence.VideoSearchRepository;
import team7.inplace.search.persistence.dto.SearchResult;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.video.application.dto.VideoInfo;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {
    private static final Integer MAX_COMPLETION_RESULTS = 5;

    private final VideoSearchRepository videoSearchRepository;
    private final InfluencerSearchRepository influencerSearchRepository;
    private final PlaceSearchRepository placeSearchRepository;
    private final FavoriteInfluencerRepository favoriteInfluencerRepository;
    private final LikedPlaceRepository likedPlaceRepository;

    public List<AutoCompletionInfo> searchAutoCompletions(String keyword) {
        var influencerSearchInfo = influencerSearchRepository.searchEntityByKeywords(keyword);
        var placeSearchInfo = placeSearchRepository.searchEntityByKeywords(keyword);

        var influencerAutoComplete = influencerSearchInfo.stream()
                .map(info -> new AutoCompletionInfo(info.searchResult().getName(), info.score(), SearchType.INFLUENCER))
                .toList();
        var placeAutoComplete = placeSearchInfo.stream()
                .map(info -> new AutoCompletionInfo(info.searchResult().getName(), info.score(), SearchType.PLACE))
                .toList();

        return Stream.concat(influencerAutoComplete.stream(), placeAutoComplete.stream())
                .sorted(Comparator.comparing(AutoCompletionInfo::score).reversed())
                .limit(MAX_COMPLETION_RESULTS)
                .toList();
    }

    public List<VideoInfo> searchVideo(String keyword) {
        var videoInfos = videoSearchRepository.searchEntityByKeywords(keyword);

        return videoInfos.stream()
                .map(videoInfo -> {
                    return VideoInfo.from(videoInfo.searchResult());
                })
                .toList();
    }

    public List<InfluencerInfo> searchInfluencer(String keyword) {
        var influencerInfos = influencerSearchRepository.searchEntityByKeywords(keyword);
        Long userId = AuthorizationUtil.getUserId();

        if (userId == null) {
            return influencerInfos.stream()
                    .map(influencer -> InfluencerInfo.from(influencer.searchResult(), false))
                    .toList();
        }

        var likedInfluencerIds = favoriteInfluencerRepository.findLikedInfluencerIdsByUserId(userId);

        return influencerInfos.stream()
                .map(influencerInfo -> {
                    boolean isLiked = likedInfluencerIds.contains(influencerInfo.searchResult().getId());
                    return InfluencerInfo.from(influencerInfo.searchResult(), isLiked);
                })
                .sorted((a, b) -> Boolean.compare(b.likes(), a.likes()))
                .toList();
    }

    public Page<InfluencerInfo> searchInfluencer(String keyword, Pageable pageable) {
        Page<SearchResult<Influencer>> influencerInfos = influencerSearchRepository.searchEntityByKeywords(keyword,
                pageable);
        Long userId = AuthorizationUtil.getUserId();

        if (userId == null) {
            return new PageImpl<>(
                    influencerInfos.getContent().stream()
                            .map(influencer -> InfluencerInfo.from(influencer.searchResult(), false))
                            .toList(),
                    pageable,
                    influencerInfos.getTotalElements()
            );
        }

        var likedInfluencerIds = favoriteInfluencerRepository.findLikedInfluencerIdsByUserId(userId);

        List<InfluencerInfo> sortedContent = influencerInfos.getContent().stream()
                .map(influencerInfo -> {
                    boolean isLiked = likedInfluencerIds.contains(influencerInfo.searchResult().getId());
                    return InfluencerInfo.from(influencerInfo.searchResult(), isLiked);
                })
                .sorted((a, b) -> Boolean.compare(b.likes(), a.likes()))
                .toList();

        return new PageImpl<>(
                sortedContent,
                pageable,
                influencerInfos.getTotalElements()
        );
    }

    public List<PlaceSearchInfo> searchPlace(String keyword) {
        var placeInfos = placeSearchRepository.searchEntityByKeywords(keyword);
        Long userId = AuthorizationUtil.getUserId();

        if (userId == null) {
            return placeInfos.stream()
                    .map(placeInfo -> PlaceSearchInfo.from(placeInfo.searchResult(), false))
                    .toList();
        }

        var likedPlaceIds = likedPlaceRepository.findPlaceIdsByUserIdAndIsLikedTrue(userId);
        return placeInfos.stream()
                .map(placeInfo -> {
                    boolean isLiked = likedPlaceIds.contains(placeInfo.searchResult().getId());
                    return PlaceSearchInfo.from(placeInfo.searchResult(), isLiked);
                })
                .sorted((a, b) -> Boolean.compare(b.likes(), a.likes()))
                .toList();
    }
}
