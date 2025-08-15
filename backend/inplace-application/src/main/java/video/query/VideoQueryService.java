package video.query;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.AuthorizationUtil;
import video.CoolVideo;
import video.RecentVideo;
import video.jpa.CoolVideoJpaRepository;
import video.jpa.RecentVideoJpaRepository;
import video.jpa.VideoJpaRepository;
import video.query.VideoQueryResult.AdminVideo;
import video.query.VideoQueryResult.DetailedVideo;
import video.query.VideoQueryResult.SimpleVideo;
import video.query.dto.VideoParam;

@Service
@RequiredArgsConstructor
public class VideoQueryService {

    private final VideoReadRepository videoReadRepository;
    private final VideoJpaRepository videoJpaRepository;
    private final CoolVideoJpaRepository coolVideoJpaRepository;
    private final RecentVideoJpaRepository recentVideoJpaRepository;

    //TODO: Facade에서 호출로 변경해야함.
    @Transactional(readOnly = true)
    public List<DetailedVideo> getVideosBySurround(
        VideoParam videoSearchParams,
        Pageable pageable,
        boolean authorizationRequired
    ) {
        // 토큰 정보에 대한 검증
        if (authorizationRequired) {
            AuthorizationUtil.checkLoginUser();
        }
        var surroundVideos = videoReadRepository.findSimpleVideosInSurround(
            Double.valueOf(videoSearchParams.topLeftLongitude()),
            Double.valueOf(videoSearchParams.topLeftLatitude()),
            Double.valueOf(videoSearchParams.bottomRightLongitude()),
            Double.valueOf(videoSearchParams.bottomRightLatitude()),
            Double.valueOf(videoSearchParams.longitude()),
            Double.valueOf(videoSearchParams.latitude()),
            pageable
        );

        return surroundVideos.getContent();
    }

    @Transactional(readOnly = true)
    public List<RecentVideo> getRecentVideos() {
        return recentVideoJpaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<CoolVideo> getCoolVideo(String parentCategoryName) {
        return coolVideoJpaRepository.findByPlaceCategoryParentName(parentCategoryName);
    }

    @Transactional(readOnly = true)
    public List<VideoQueryResult.DetailedVideo> getMyInfluencerVideos(Long userId) {
        var top10Videos = videoReadRepository.findTop10ByLikedInfluencer(userId);

        return top10Videos.stream().toList();
    }

    @Transactional(readOnly = true)
    public Page<DetailedVideo> getOneInfluencerVideos(
        Long influencerId, Pageable pageable) {
        var videos = videoReadRepository.findDetailedVideosWithOneInfluencerId(influencerId,
            pageable);
        return videos;
    }

    @Transactional(readOnly = true)
    public Map<Long, List<SimpleVideo>> getVideosByPlaceId(List<Long> placeIds) {
        return videoReadRepository.findSimpleVideosByPlaceIds(placeIds);
    }

    @Transactional(readOnly = true)
    public List<SimpleVideo> getVideosByPlaceId(Long placeId) {
        return videoReadRepository.findSimpleVideosByPlaceId(placeId);
    }

    @Transactional(readOnly = true)
    public Page<AdminVideo> getAdminVideosByCondition(
        VideoQueryParam videoQueryParam, Pageable pageable) {
        return videoReadRepository.findAdminVideoByCondition(videoQueryParam, pageable);
    }

    @Transactional(readOnly = true)
    public Page<SimpleVideo> getVideoWithNoPlace(Pageable pageable) {
        return videoReadRepository.findVideoWithNoPlace(pageable);
    }
}
