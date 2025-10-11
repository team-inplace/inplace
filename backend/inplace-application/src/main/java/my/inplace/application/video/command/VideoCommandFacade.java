package my.inplace.application.video.command;

import my.inplace.application.annotation.Facade;
import my.inplace.application.influencer.command.InfluencerCommandService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.application.place.query.PlaceQueryService;
import my.inplace.application.video.command.dto.VideoCommand;
import my.inplace.application.video.command.dto.VideoCommand.Create;

@Facade
@Slf4j
@RequiredArgsConstructor
public class VideoCommandFacade {
    private final VideoCommandService videoCommandService;
    private final InfluencerCommandService influencerCommandService;
    private final PlaceQueryService placeQueryService;

    @Transactional
    public void createMediumVideos(
        List<Create> videoCommands,
        Long influencerId
    ) {
        videoCommandService.createVideos(videoCommands);
        influencerCommandService.updateLastMediumVideo(influencerId, videoCommands.get(0).videoId());
    }

    @Transactional
    public void createLongVideos(
        List<VideoCommand.Create> videoCommands,
        Long influencerId
    ) {
        videoCommandService.createVideos(videoCommands);
        influencerCommandService.updateLastLongVideo(influencerId, videoCommands.get(0).videoId());
    }

    @Transactional
    public void updateVideoViews(List<VideoCommand.UpdateViewCount> videoCommands) {
        videoCommandService.updateVideoViews(videoCommands);
    }

    @Transactional
    public void updateCoolVideos() {
        List<Long> parentCategoryIds = placeQueryService.getParentCategoryIds();
        videoCommandService.updateCoolVideos(parentCategoryIds);
    }

    @Transactional
    public void updateRecentVideos() {
        videoCommandService.updateRecentVideos();
    }
    
    @Transactional
    public void deleteVideo(Long videoId) {
        videoCommandService.deleteVideo(videoId);
    }
}
