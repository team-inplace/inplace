package team7.inplace.video.persistence.dto;

import com.querydsl.core.annotations.QueryProjection;
import team7.inplace.place.domain.Category;
import team7.inplace.video.domain.CoolVideo;
import team7.inplace.video.domain.RecentVideo;

public class VideoQueryResult {
    public record SimpleVideo(
            Long videoId,
            String videoUUID,
            String influencerName,
            Long placeId,
            String placeName,
            Category placeCategory
    ) {
        @QueryProjection
        public SimpleVideo {
        }

        public String videoUrl() {
            return "https://www.youtube.com/watch?v=" + videoUUID;
        }

        public CoolVideo toCoolVideo() {
            return CoolVideo.from(videoId, videoUUID, influencerName, placeId, placeName, placeCategory);
        }

        public static SimpleVideo from(CoolVideo coolVideo) {
            return new SimpleVideo(
                coolVideo.getVideoId(),
                coolVideo.getVideoUUID(),
                coolVideo.getInfluencerName(),
                coolVideo.getPlaceId(),
                coolVideo.getPlaceName(),
                coolVideo.getPlaceCategory()
            );
        }

        public RecentVideo toRecentVideo() {
            return RecentVideo.from(videoId, videoUUID, influencerName, placeId, placeName, placeCategory);
        }

        public static SimpleVideo from(RecentVideo recentVideo) {
            return new SimpleVideo(
                recentVideo.getVideoId(),
                recentVideo.getVideoUUID(),
                recentVideo.getInfluencerName(),
                recentVideo.getPlaceId(),
                recentVideo.getPlaceName(),
                recentVideo.getPlaceCategory()
            );
        }
    }
}
