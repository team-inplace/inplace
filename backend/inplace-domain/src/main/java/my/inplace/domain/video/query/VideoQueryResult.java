package my.inplace.domain.video.query;

import org.springframework.lang.Nullable;
import my.inplace.domain.video.CoolVideo;
import my.inplace.domain.video.RecentVideo;

public class VideoQueryResult {

    public record SimpleVideo(
        Long videoId,
        String videoUUID,
        String influencerName,
        Long placeId,
        String placeName,
        String placeCategory
    ) {

        public String videoUrl() {
            return "https://www.youtube.com/watch?v=" + videoUUID;
        }
    }

    public record DetailedVideo(
        Long videoId,
        String videoUUID,
        String influencerName,
        Long placeId,
        String placeName,
        String placeCategoryParentName,
        @Nullable Long placeCategoryParentId,
        String address1,
        String address2,
        String address3
    ) {

        public String videoUrl() {
            return "https://www.youtube.com/watch?v=" + videoUUID;
        }

        public CoolVideo toCoolVideo() {
            return CoolVideo.from(videoId, videoUUID, influencerName, placeId, placeName,
                placeCategoryParentName, placeCategoryParentId, address1, address2, address3);
        }

        public RecentVideo toRecentVideo() {
            return RecentVideo.from(videoId, videoUUID, influencerName, placeId, placeName,
                placeCategoryParentName, address1, address2, address3);
        }
    }

    public record AdminVideo(
        Long videoId,
        String videoUUID,
        Boolean registered
    ) {

    }
}
