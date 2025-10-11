package my.inplace.application.crawling.dto;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import my.inplace.application.video.command.dto.VideoCommand;

public class CrawlingInfo {

    public record VideoPlaceInfo(
        Long influencerId,
        List<JsonNode> mediumItems,
        List<JsonNode> longItems
    ) {

        public List<VideoCommand.Create> toMediumVideoCommands() {
            return mediumItems.stream()
                .map(videoItem -> VideoCommand.Create.from(videoItem, influencerId))
                .toList();
        }

        public List<VideoCommand.Create> toLongVideoCommands() {
            return longItems.stream()
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
            return VideoCommand.UpdateViewCount.from(
                videoDetail().path("items").get(0).get("statistics"), videoId());
        }
    }
}
