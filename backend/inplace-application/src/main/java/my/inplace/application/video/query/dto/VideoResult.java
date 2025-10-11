package my.inplace.application.video.query.dto;

import my.inplace.domain.video.CoolVideo;
import my.inplace.domain.video.RecentVideo;
import my.inplace.domain.video.query.VideoQueryResult;

public class VideoResult {

    public record Admin(
        Long id,
        String uuid,
        Boolean registered
    ) {

        public static Admin from(VideoQueryResult.AdminVideo videoInfo) {
            return new Admin(
                videoInfo.videoId(),
                videoInfo.videoUUID(),
                videoInfo.registered()
            );
        }
    }

    public record SimpleVideo(
        Long videoId,
        String videoUUID,
        String influencerName,
        Long placeId,
        String placeName,
        String placeCategory,
        String videoUrl
    ) {

        public static SimpleVideo from(VideoQueryResult.SimpleVideo videoInfo) {
            return new SimpleVideo(
                videoInfo.videoId(),
                videoInfo.videoUUID(),
                videoInfo.influencerName(),
                videoInfo.placeId(),
                videoInfo.placeName(),
                videoInfo.placeCategory(),
                videoInfo.videoUrl()
            );
        }
    }

    public record DetailedVideo(
        Long videoId,
        String videoUUID,
        String influencerName,
        Long placeId,
        String placeName,
        String placeCategoryParentName,
        Long placeCategoryParentId,
        String address1,
        String address2,
        String address3,
        String videoUrl
    ) {

        public static DetailedVideo from(VideoQueryResult.DetailedVideo videoInfo) {
            return new DetailedVideo(
                videoInfo.videoId(),
                videoInfo.videoUUID(),
                videoInfo.influencerName(),
                videoInfo.placeId(),
                videoInfo.placeName(),
                videoInfo.placeCategoryParentName(),
                videoInfo.placeCategoryParentId(),
                videoInfo.address1(),
                videoInfo.address2(),
                videoInfo.address3(),
                videoInfo.videoUrl()
            );
        }

        public static DetailedVideo from(CoolVideo video) {
            return new DetailedVideo(
                video.getVideoId(),
                video.getVideoUUID(),
                video.getInfluencerName(),
                video.getPlaceId(),
                video.getPlaceName(),
                video.getPlaceCategoryParentName(),
                video.getPlaceCategoryParentId(),
                video.getAddress1(),
                video.getAddress2(),
                video.getAddress3(),
                video.getVideoUrl()
            );
        }

        public static DetailedVideo from(RecentVideo video) {
            return new DetailedVideo(
                video.getVideoId(),
                video.getVideoUUID(),
                video.getInfluencerName(),
                video.getPlaceId(),
                video.getPlaceName(),
                video.getPlaceCategoryParentName(),
                null,
                video.getAddress1(),
                video.getAddress2(),
                video.getAddress3(),
                video.getVideoUrl()
            );
        }
    }
}
