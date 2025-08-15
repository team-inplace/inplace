package video.command.dto;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import video.Video;

@Slf4j
public class VideoCommand {

    public record Create(
        String videoId,
        String videoTitle,
        LocalDateTime createdAt,
        Long influencerId
    ) {

        public static Create from(JsonNode videoItem, Long influencerId) {
            try {
                String videoId = videoItem.get("id").get("videoId").asText();
                String videoTitle = videoItem.get("snippet").get("title").asText();
                String publishedAt = videoItem.get("snippet").get("publishedAt").asText();
                LocalDateTime createdAt = LocalDateTime.parse(publishedAt,
                    DateTimeFormatter.ISO_DATE_TIME);
                return new Create(videoId, videoTitle, createdAt, influencerId);
            } catch (Exception e) {
                log.error("비디오 크롤링 커맨드생성 에러 , VideoItem : {}, 에러 {}", videoItem, e.getMessage());
            }
            return null;
        }

        public Video toEntity() {
            return Video.from(videoId, createdAt, influencerId);
        }
    }

    public record UpdateViewCount(
        Long videoId,
        Long viewCount
    ) {

        public static UpdateViewCount from(JsonNode statistics, Long videoId) {
            Long viewCount = statistics.path("viewCount").asLong();

            return new UpdateViewCount(videoId, viewCount);
        }
    }
}
