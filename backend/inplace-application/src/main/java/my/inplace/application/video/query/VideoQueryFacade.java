package my.inplace.application.video.query;

import my.inplace.application.annotation.Facade;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.security.util.AuthorizationUtil;
import my.inplace.application.video.query.dto.VideoParam;
import my.inplace.application.video.query.dto.VideoResult;
import my.inplace.application.video.query.dto.VideoResult.DetailedVideo;
import my.inplace.application.video.query.dto.VideoResult.SimpleVideo;

@Facade
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoQueryFacade {

    private final VideoQueryService videoQueryService;

    public List<DetailedVideo> getMyInfluencerVideos() {
        // User 정보를 쿠키에서 추출
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        // 인플루언서 id를 사용하여 영상을 조회
        return videoQueryService.getMyInfluencerVideos(userId);
    }

    public Page<SimpleVideo> getVideoWithNoPlace(Pageable pageable) {
        return videoQueryService.getVideoWithNoPlace(pageable);
    }

    public Page<VideoResult.Admin> getAdminVideosByCondition(
        VideoParam.Condition condition, Pageable pageable
    ) {
        return videoQueryService.getAdminVideosByCondition(condition, pageable);
    }
}
