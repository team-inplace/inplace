package team7.inplace.video.application.command;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import team7.inplace.video.domain.Video;

public class VideoCommand {

    public record Create(
        String videoId,
        String videoTitle,
        LocalDateTime createdAt,
        Long influencerId
    ) {

        public static Create from(JsonNode videoItem, Long influencerId) {
            String videoId = videoItem.get("id").get("videoId").asText();
            String videoTitle = videoItem.get("snippet").get("title").asText();
            String publishedAt = videoItem.get("snippet").get("publishedAt").asText();
            LocalDateTime createdAt = LocalDateTime.parse(publishedAt,
                DateTimeFormatter.ISO_DATE_TIME);
            return new Create(videoId, videoTitle, createdAt, influencerId);
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
