package my.inplace.application.video.query;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.video.query.VideoQueryResult;
import my.inplace.domain.video.query.VideoReadRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.security.util.AuthorizationUtil;
import my.inplace.infra.video.jpa.CoolVideoJpaRepository;
import my.inplace.infra.video.jpa.RecentVideoJpaRepository;
import my.inplace.application.video.query.dto.VideoParam;
import my.inplace.application.video.query.dto.VideoParam.SquareBound;
import my.inplace.application.video.query.dto.VideoResult;
import my.inplace.application.video.query.dto.VideoResult.Admin;
import my.inplace.application.video.query.dto.VideoResult.DetailedVideo;
import my.inplace.application.video.query.dto.VideoResult.SimpleVideo;

@Service
@RequiredArgsConstructor
public class VideoQueryService {

    private final VideoReadRepository videoReadRepository;
    private final CoolVideoJpaRepository coolVideoJpaRepository;
    private final RecentVideoJpaRepository recentVideoJpaRepository;

    //TODO: Facade에서 호출로 변경해야함.
    @Transactional(readOnly = true)
    public List<DetailedVideo> getVideosBySurround(
        SquareBound videoSearchParams,
        Pageable pageable,
        boolean authorizationRequired
    ) {
        // 토큰 정보에 대한 검증
        if (authorizationRequired) {
            AuthorizationUtil.checkLoginUser();
        }
        var surroundVideos = videoReadRepository.findSimpleVideosInSurround(
            videoSearchParams.toQueryParam(),
            pageable
        );

        return surroundVideos.getContent().stream().map(DetailedVideo::from).toList();
    }

    @Transactional(readOnly = true)
    public List<VideoResult.DetailedVideo> getRecentVideos() {
        return recentVideoJpaRepository.findAll()
            .stream()
            .map(DetailedVideo::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<VideoResult.DetailedVideo> getCoolVideo(String parentCategoryName) {
        return coolVideoJpaRepository.findByPlaceCategoryParentName(parentCategoryName)
            .stream()
            .map(DetailedVideo::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<VideoResult.DetailedVideo> getMyInfluencerVideos(Long userId) {
        var top10Videos = videoReadRepository.findTop10ByLikedInfluencer(userId);

        return top10Videos.stream().map(DetailedVideo::from).toList();
    }

    @Transactional(readOnly = true)
    public Page<DetailedVideo> getOneInfluencerVideos(
        Long influencerId, Pageable pageable) {
        var videos = videoReadRepository.findDetailedVideosWithOneInfluencerId(influencerId,
            pageable);
        return videos.map(DetailedVideo::from);
    }

    @Transactional(readOnly = true)
    public Map<Long, List<VideoQueryResult.SimpleVideo>> getVideosByPlaceId(List<Long> placeIds) {
        return videoReadRepository.findSimpleVideosByPlaceIds(placeIds);
    }

    @Transactional(readOnly = true)
    public List<SimpleVideo> getVideosByPlaceId(Long placeId) {
        return videoReadRepository.findSimpleVideosByPlaceId(placeId).stream()
            .map(SimpleVideo::from).toList();
    }

    @Transactional(readOnly = true) // Todo videoQueryParam? videoParam?
    public Page<Admin> getAdminVideosByCondition(
        VideoParam.Condition condition, Pageable pageable
    ) {
        return videoReadRepository.findAdminVideoByCondition(condition.toQueryParam(), pageable)
            .map(Admin::from);
    }

    @Transactional(readOnly = true)
    public Page<SimpleVideo> getVideoWithNoPlace(Pageable pageable) {
        return videoReadRepository.findVideoWithNoPlace(pageable).map(SimpleVideo::from);
    }
}
