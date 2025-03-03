package team7.inplace.admin.crawling.client;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import team7.inplace.global.properties.GoogleApiProperties;

@Slf4j
@Component
@RequiredArgsConstructor
public class YoutubeClient {

    private static final String VIDEO_SEARCH_URL = "https://www.googleapis.com/youtube/v3/search";
    private static final String MEDIUM_VIDEO_SEARCH_PARAMS = "?part=snippet&channelId=%s&maxResults=50&key=%s&order=date&type=video&videoDuration=medium";
    private static final String LONG_VIDEO_SEARCH_PARAMS = "?part=snippet&channelId=%s&maxResults=50&key=%s&order=date&type=video&videoDuration=long";
    private static final String VIDEO_DETAIL_URL = "https://www.googleapis.com/youtube/v3/videos";
    private static final String VIDEO_DETAIL_PARAMS = "?part=statistics&id=%s&key=%s";
    private final RestTemplate restTemplate;
    private final GoogleApiProperties googleApiProperties;

    public JsonNode getVideoDetail(String videoId) {
        String url = VIDEO_DETAIL_URL + String.format(VIDEO_DETAIL_PARAMS, videoId,
            googleApiProperties.crawlingKey());

        JsonNode response = null;
        try {
            response = restTemplate.getForObject(url, JsonNode.class);
        } catch (Exception e) {
            log.error("Youtube API 호출이 실패했습니다. Video Id {}", videoId);
        }

        log.info(response.toPrettyString());

        return response;
    }

    public List<JsonNode> getMediumVideos(String chanelId, String finalVideoUUID) {
        List<JsonNode> videoItems = new ArrayList<>();
        String nextPageToken = null;
        var key = Arrays.stream(googleApiProperties.getGoogleKey()).iterator();
        var curKey = key.next();
        while (true) {
            String url =
                VIDEO_SEARCH_URL + String.format(MEDIUM_VIDEO_SEARCH_PARAMS, chanelId, curKey);

            JsonNode response = null;
            if (Objects.nonNull(nextPageToken)) {
                url += "&pageToken=" + nextPageToken;
            }
            try {
                response = restTemplate.getForObject(url, JsonNode.class);
            } catch (Exception e) {
                log.error("Youtube API 호출이 실패했습니다. Youtuber Id {}", chanelId);
                break;
            }

            if (response.path("error").path("code").asText().equals("403")) {
                if (key.hasNext()) {
                    curKey = key.next();
                    continue;
                }
                log.error("Youtube API 호출이 실패했습니다. Youtuber Id {}", chanelId);
                break;
            }
            if (Objects.isNull(response)) {
                log.error("Youtube API Response가 NULL입니다 {}.", chanelId);
                break;
            }

            var containsLastVideo = extractSnippets(videoItems, response.path("items"),
                finalVideoUUID);
            if (containsLastVideo) {
                break;
            }
            nextPageToken = response.path("nextPageToken").asText();
            if (isLastPage(nextPageToken)) {
                break;
            }
        }
        return videoItems;
    }

    public List<JsonNode> getLongVidoes(String chanelId, String finalVideoUUID) {
        List<JsonNode> videoItems = new ArrayList<>();
        String nextPageToken = null;
        while (true) {
            String url = VIDEO_SEARCH_URL + String.format(LONG_VIDEO_SEARCH_PARAMS, chanelId,
                googleApiProperties.crawlingKey());

            JsonNode response = null;
            if (Objects.nonNull(nextPageToken)) {
                url += "&pageToken=" + nextPageToken;
            }
            try {
                response = restTemplate.getForObject(url, JsonNode.class);
            } catch (Exception e) {
                log.error("Youtube API 호출이 실패했습니다. Youtuber Id {}", chanelId);
                break;
            }
            if (Objects.isNull(response)) {
                log.error("Youtube API Response가 NULL입니다 {}.", chanelId);
                break;
            }

            var containsLastVideo = extractSnippets(videoItems, response.path("items"),
                finalVideoUUID);
            if (containsLastVideo) {
                break;
            }
            nextPageToken = response.path("nextPageToken").asText();
            if (isLastPage(nextPageToken)) {
                break;
            }
        }
        return videoItems;
    }

    private boolean isLastPage(String nextPageToken) {
        return Objects.isNull(nextPageToken) || nextPageToken.isEmpty();
    }

    private boolean extractSnippets(
        List<JsonNode> videoItems, JsonNode items, String finalVideoUUID) {

        for (JsonNode item : items) {
            if (!item.get("id").has("videoId")) {
                continue;
            }
            var videoId = item.get("id").get("videoId").asText();

            if (videoId.equals(finalVideoUUID)) {
                return true;
            }
            videoItems.add(item);
        }
        return false;
    }
}
