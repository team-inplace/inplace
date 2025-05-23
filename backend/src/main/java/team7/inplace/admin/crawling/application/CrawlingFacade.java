package team7.inplace.admin.crawling.application;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import team7.inplace.admin.crawling.application.dto.CrawlingInfo;
import team7.inplace.global.annotation.Facade;
import team7.inplace.video.application.VideoFacade;

@Facade
@Slf4j
@RequiredArgsConstructor
public class CrawlingFacade {

    private final YoutubeCrawlingService youtubeCrawlingService;
    private final VideoFacade videoFacade;

    @Scheduled(cron = "${crawling.updateVideoTimeCron}", zone = "Asia/Seoul")
    public void updateVideos() {
        var crawlingInfos = youtubeCrawlingService.crawlAllVideos();
        for (var crawlingInfo : crawlingInfos) {
            var mediumVideoCommands = crawlingInfo.toMediumVideoCommands()
                .stream()
                .filter(Objects::nonNull)
                .sorted((a, b) -> b.createdAt().compareTo(a.createdAt()))
                .toList();

            if (mediumVideoCommands.isEmpty()) {
                continue;
            }
            videoFacade.createMediumVideos(mediumVideoCommands, crawlingInfo.influencerId());

            var longVideoCommands = crawlingInfo.toLongVideoCommands()
                .stream()
                .filter(Objects::nonNull)
                .sorted((a, b) -> b.createdAt().compareTo(a.createdAt()))
                .toList();
            if (longVideoCommands.isEmpty()) {
                continue;
            }
            videoFacade.createLongVideos(longVideoCommands, crawlingInfo.influencerId());
        }
    }

    @Scheduled(cron = "${crawling.updateVideoViewTimeCron}", zone = "Asia/Seoul")
    public void updateVideoView() {
        var crawlingInfos = youtubeCrawlingService.crawlingVideoView();
        var videoCommands = crawlingInfos.stream()
            .map(CrawlingInfo.ViewInfo::toVideoCommand)
            .toList();

        videoFacade.updateVideoViews(videoCommands);
    }
}
