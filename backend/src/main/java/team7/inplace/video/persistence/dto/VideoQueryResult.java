package team7.inplace.video.persistence.dto;

import com.querydsl.core.annotations.QueryProjection;
import team7.inplace.video.domain.CoolVideo;
import team7.inplace.video.domain.RecentVideo;

public class VideoQueryResult {

    public record SimpleVideo(
        Long videoId,
        String videoUUID,
        String influencerName,
        Long placeId,
        String placeName,
        String placeCategory
    ) {

        @QueryProjection
        public SimpleVideo {
        }

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
        String placeCategory,
        String address1,
        String address2,
        String address3
    ) {

        @QueryProjection
        public DetailedVideo {
        }

        public static DetailedVideo from(CoolVideo coolVideo) {
            return new DetailedVideo(
                coolVideo.getVideoId(),
                coolVideo.getVideoUUID(),
                coolVideo.getInfluencerName(),
                coolVideo.getPlaceId(),
                coolVideo.getPlaceName(),
                coolVideo.getPlaceCategory(),
                coolVideo.getAddress1(),
                coolVideo.getAddress2(),
                coolVideo.getAddress3()
            );
        }

        public static DetailedVideo from(RecentVideo recentVideo) {
            return new DetailedVideo(
                recentVideo.getVideoId(),
                recentVideo.getVideoUUID(),
                recentVideo.getInfluencerName(),
                recentVideo.getPlaceId(),
                recentVideo.getPlaceName(),
                recentVideo.getPlaceCategory(),
                recentVideo.getAddress1(),
                recentVideo.getAddress2(),
                recentVideo.getAddress3()
            );
        }

        public String videoUrl() {
            return "https://www.youtube.com/watch?v=" + videoUUID;
        }

        public CoolVideo toCoolVideo() {
            return CoolVideo.from(videoId, videoUUID, influencerName, placeId, placeName,
                placeCategory, address1, address2, address3);
        }

        public RecentVideo toRecentVideo() {
            return RecentVideo.from(videoId, videoUUID, influencerName, placeId, placeName,
                placeCategory, address1, address2, address3);
        }
    }

    public record AdminVideo(
        Long videoId,
        String videoUUID
    ) {

        @QueryProjection
        public AdminVideo {
        }

    }
}
