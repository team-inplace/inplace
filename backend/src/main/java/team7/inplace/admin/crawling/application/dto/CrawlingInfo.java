package team7.inplace.admin.crawling.application.dto;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import team7.inplace.video.application.command.VideoCommand;

public class CrawlingInfo {
    public record VideoPlaceInfo(
            Long influencerId,
            List<JsonNode> videoItems
    ) {
        public List<VideoCommand.Create> toVideoCommands() {
            return videoItems.stream()
                    .map(videoItem -> VideoCommand.Create.from(videoItem, influencerId))
                    .toList();
        }
    }

    public record ViewInfo(
            Long videoId,
            JsonNode videoDetail
    ) {
        public static ViewInfo of(Long videoId, JsonNode videoDetail) {
            return new ViewInfo(videoId, videoDetail);
        }

        public VideoCommand.UpdateViewCount toVideoCommand() {
            return VideoCommand.UpdateViewCount.from(videoDetail().path("items").get(0).get("statistics"), videoId());
        }
    }
}
