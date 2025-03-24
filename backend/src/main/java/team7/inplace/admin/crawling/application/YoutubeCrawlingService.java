package team7.inplace.admin.crawling.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.admin.crawling.application.dto.CrawlingInfo;
import team7.inplace.admin.crawling.application.dto.CrawlingInfo.ViewInfo;
import team7.inplace.admin.crawling.client.YoutubeClient;
import team7.inplace.influencer.persistence.InfluencerRepository;
import team7.inplace.video.persistence.VideoRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeCrawlingService {

    private final InfluencerRepository influencerRepository;
    private final VideoRepository videoRepository;
    private final YoutubeClient youtubeClient;

    /*
        1. 유튜브 채널 정보를 모두 가져온다.
        2. 마지막 비디오와, 유튜브 UUID를 이용하여 비디오 정보를 가져온다.
        3. 마지막 비디오 UUID를 업데이트 한다.
        4. 카카오 API를 호출해 장소 정보를 가져온다
     */
    public List<CrawlingInfo.VideoPlaceInfo> crawlAllVideos() {
        var influencers = influencerRepository.findAllByOrderByUpdateAtAsc();
        var crawlInfos = influencers.stream()
            .map(influencer -> {
                var channel = influencer.getChannelId();
                var lastMediumVideoId = influencer.getLastMediumVideo();
                var lastLongVideoId = influencer.getLastLongVideo();

                var mediumVideoItems = youtubeClient.getMediumVideos(channel, lastMediumVideoId);
                var longVideoItems = youtubeClient.getLongVidoes(channel, lastLongVideoId);
                return new CrawlingInfo.VideoPlaceInfo(influencer.getId(), mediumVideoItems,
                    longVideoItems);
            }).toList();

        return crawlInfos;
    }

    @Transactional(readOnly = true)
    public List<ViewInfo> crawlingVideoView() {
        var videos = videoRepository.findAll();

        var videoInfos = videos.stream().map(video -> {
            var videoId = video.getId();
            var videoUUID = video.getUuid();
            var videoDetail = youtubeClient.getVideoDetail(videoUUID);
            return CrawlingInfo.ViewInfo.of(videoId, videoDetail);
        }).toList();

        return videoInfos;
    }
}

