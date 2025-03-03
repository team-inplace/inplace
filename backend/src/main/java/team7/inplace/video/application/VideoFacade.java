package team7.inplace.video.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.annotation.Facade;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;
import team7.inplace.influencer.application.InfluencerService;
import team7.inplace.place.application.PlaceService;
import team7.inplace.place.application.command.PlacesCommand;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.video.application.command.VideoCommand;
import team7.inplace.video.persistence.dto.VideoQueryResult;

@Facade
@Slf4j
@RequiredArgsConstructor
public class VideoFacade {

    private final VideoService videoService;
    private final InfluencerService influencerService;
    private final PlaceService placeService;

    @Transactional
    public void createMediumVideos(
        List<VideoCommand.Create> videoCommands,
        Long influencerId
    ) {
        videoService.createVideos(videoCommands, influencerId);
        influencerService.updateLastMediumVideo(influencerId, videoCommands.get(0).videoId());
    }

    @Transactional
    public void createLongVideos(
        List<VideoCommand.Create> videoCommands,
        Long influencerId
    ) {
        videoService.createVideos(videoCommands, influencerId);
        influencerService.updateLastLongVideo(influencerId, videoCommands.get(0).videoId());
    }

    @Transactional
    public void updateVideoViews(List<VideoCommand.UpdateViewCount> videoCommands) {
        videoService.updateVideoViews(videoCommands);
    }

    @Transactional
    public void addPlaceInfo(Long videoId, PlacesCommand.Create placeCommand) {
        var placeId = placeService.createPlace(placeCommand);
        videoService.addPlaceInfo(videoId, placeId);
    }

    @Transactional(readOnly = true)
    public List<VideoQueryResult.SimpleVideo> getMyInfluencerVideos() {
        // 토큰 정보에 대한 검증
        if (AuthorizationUtil.isNotLoginUser()) {
            throw InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY);
        }
        // User 정보를 쿠키에서 추출
        Long userId = AuthorizationUtil.getUserId();
        // 인플루언서 id를 사용하여 영상을 조회
        return videoService.getMyInfluencerVideos(userId);
    }

    @Transactional(readOnly = true)
    public Page<VideoQueryResult.SimpleVideo> getVideoWithNoPlace(Pageable pageable) {
        return videoService.getVideoWithNoPlace(pageable);
    }
}
