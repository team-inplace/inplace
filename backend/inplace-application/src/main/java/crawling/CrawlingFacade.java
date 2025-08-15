package crawling;

import java.util.Objects;

import annotation.Facade;
import crawling.dto.CrawlingInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import video.command.VideoCommandFacade;

@Facade
@Slf4j
@RequiredArgsConstructor
public class CrawlingFacade {

    private final YoutubeCrawlingService youtubeCrawlingService;
    private final VideoCommandFacade videoCommandFacade;

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
            videoCommandFacade.createMediumVideos(mediumVideoCommands, crawlingInfo.influencerId());

            var longVideoCommands = crawlingInfo.toLongVideoCommands()
                .stream()
                .filter(Objects::nonNull)
                .sorted((a, b) -> b.createdAt().compareTo(a.createdAt()))
                .toList();
            if (longVideoCommands.isEmpty()) {
                continue;
            }
            videoCommandFacade.createLongVideos(longVideoCommands, crawlingInfo.influencerId());
        }
    }

    @Scheduled(cron = "${crawling.updateVideoViewTimeCron}", zone = "Asia/Seoul")
    public void updateVideoView() {
        var crawlingInfos = youtubeCrawlingService.crawlingVideoView();
        var videoCommands = crawlingInfos.stream()
            .map(CrawlingInfo.ViewInfo::toVideoCommand)
            .toList();

        videoCommandFacade.updateVideoViews(videoCommands);
    }
}
