package my.inplace.infra.crawling;

import my.inplace.infra.annotation.Client;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import my.inplace.common.properties.GoogleApiProperties;

@Slf4j
@RequiredArgsConstructor
@Client("Youtube Crawling Client")
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

        return response;
    }

    public List<JsonNode> getMediumVideos(String channelId, String finalVideoUUID) {
        return getVideos(channelId, finalVideoUUID, MEDIUM_VIDEO_SEARCH_PARAMS);
    }

    public List<JsonNode> getLongVideos(String channelId, String finalVideoUUID) {
        return getVideos(channelId, finalVideoUUID, LONG_VIDEO_SEARCH_PARAMS);
    }

    private List<JsonNode> getVideos(String channelId, String finalVideoUUID, String searchParams) {
        List<JsonNode> videoItems = new ArrayList<>();
        Iterator<String> keyIterator = Arrays.stream(googleApiProperties.getGoogleKey()).iterator();
        String currentKey = keyIterator.next();
        String nextPageToken = null;

        while (true) {
            String url = buildSearchUrl(channelId, currentKey, nextPageToken, searchParams);

            JsonNode response = null;
            try {
                response = restTemplate.getForObject(url, JsonNode.class);
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.FORBIDDEN && keyIterator.hasNext()) {
                    log.warn("403 Forbidden: 키 변경  channelId: {}", channelId);
                    currentKey = keyIterator.next();
                    continue;
                }
                log.error("Youtube Key 만료 - channelId: {}, 상태: {}", channelId, e.getStatusCode());
                break;
            } catch (RestClientException e) {
                log.error("Youtube API 예외 발생 - channelId: {}, message: {}", channelId,
                    e.getMessage());
                break;
            } catch (Exception e) {
                log.error("알 수 없는 예외 - channelId: {}, error: {}", channelId, e.getMessage());
                break;
            }

            if (Objects.isNull(response)) {
                log.error("Youtube API 응답 NULL - channelId: {}", channelId);
                break;
            }

            boolean containsLastVideo = extractSnippets(videoItems, response.path("items"),
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

    private String buildSearchUrl(
        String channelId, String apiKey, String pageToken, String searchParams) {
        String url = VIDEO_SEARCH_URL + String.format(searchParams, channelId, apiKey);
        return (pageToken != null && !pageToken.isEmpty()) ? url + "&pageToken=" + pageToken : url;
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
