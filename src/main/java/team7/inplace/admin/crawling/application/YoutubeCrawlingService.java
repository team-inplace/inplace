package team7.inplace.admin.crawling.application;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.admin.crawling.client.KakaoMapClient;
import team7.inplace.admin.crawling.client.YoutubeClient;
import team7.inplace.admin.crawling.persistence.YoutubeChannelRepository;
import team7.inplace.admin.crawling.application.dto.CrawlingInfo;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.ChannelErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeCrawlingService {
    private final YoutubeChannelRepository youtubeChannelRepository;
    private final YoutubeClient youtubeClient;
    private final KakaoMapClient kakaoMapClient;

    /*
        1. 유튜브 채널 정보를 모두 가져온다.
        2. 마지막 비디오와, 유튜브 UUID를 이용하여 비디오 정보를 가져온다.
        3. 마지막 비디오 UUID를 업데이트 한다.
        4. 카카오 API를 호출해 장소 정보를 가져온다
     */
    public List<CrawlingInfo.VideoPlaceInfo> crawlAllVideos() {
        var youtubeChannels = youtubeChannelRepository.findAll();

        var crawlInfos = youtubeChannels.stream()
                .map(channel -> {
                    var videoSnippets = youtubeClient.getVideos(channel.getPlayListUUID(), channel.getLastVideoUUID());

                    var videoAddresses = videoSnippets.stream()
                            .map(AddressUtil::extractAddressFromJsonNode)
                            .toList();

                    var placeNodes = videoAddresses.stream()
                            .map(address -> {
                                if (Objects.isNull(address)) {
                                    return null;
                                }
                                return kakaoMapClient.searchPlaceWithAddress(address,
                                        channel.getChannelType().getCode());
                            })
                            .toList();

                    return new CrawlingInfo.VideoPlaceInfo(channel.getInfluencerId(), channel.getPlayListUUID(),
                            videoSnippets, placeNodes);
                }).toList();

        return crawlInfos;
    }

    @Transactional
    public void updateLastVideoUUID(String playListUUID, String lastVideoUUID) {
        var youtubeChannel = youtubeChannelRepository.findYoutubeChannelByPlayListUUID(playListUUID)
                .orElseThrow(() -> InplaceException.of(ChannelErrorCode.CHANNEL_NOT_FOUND));

        youtubeChannel.updateLastVideoUUID(lastVideoUUID);
    }
}

