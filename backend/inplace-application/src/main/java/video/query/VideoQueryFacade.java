package video.query;

import annotation.Facade;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import util.AuthorizationUtil;
import video.query.VideoQueryResult.DetailedVideo;
import video.query.VideoQueryResult.SimpleVideo;

@Facade
@Slf4j
@RequiredArgsConstructor
public class VideoQueryFacade {
    private final VideoQueryService videoQueryService;

    @Transactional(readOnly = true)
    public List<DetailedVideo> getMyInfluencerVideos() {
        // User 정보를 쿠키에서 추출
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        // 인플루언서 id를 사용하여 영상을 조회
        return videoQueryService.getMyInfluencerVideos(userId);
    }

    @Transactional(readOnly = true)
    public Page<SimpleVideo> getVideoWithNoPlace(Pageable pageable) {
        return videoQueryService.getVideoWithNoPlace(pageable);
    }
}
