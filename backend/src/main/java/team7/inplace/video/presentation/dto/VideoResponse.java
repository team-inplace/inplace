package team7.inplace.video.presentation.dto;

import team7.inplace.video.domain.CoolVideo;
import team7.inplace.video.domain.RecentVideo;
import team7.inplace.video.persistence.dto.VideoQueryResult;

// Video 엔티티의 Controller Response 정보 전달을 담당하는 클래스
public class VideoResponse {
    public record Simple(
            Long videoId,
            String videoUrl,
            VideoResponse.Place place
    ) {
        public static VideoResponse.Simple from(VideoQueryResult.SimpleVideo videoInfo) {
            var place = new VideoResponse.Place(
                    videoInfo.placeId(),
                    videoInfo.placeName()
            );
            return new VideoResponse.Simple(
                    videoInfo.videoId(),
                    videoInfo.videoUrl(),
                    place
            );
        }
    }

    public record Place(
            Long placeId,
            String placeName
    ) {
    }

    public record Detail(
        Long videoId,
        String influencerName,
        String videoUrl,
        VideoResponse.PlaceDetail place
    ) {
        public static VideoResponse.Detail from(VideoQueryResult.DetailedVideo videoInfo) {
            var place = new VideoResponse.PlaceDetail(
                videoInfo.placeId(),
                videoInfo.placeName(),
                new Address(
                    videoInfo.address1(),
                    videoInfo.address2(),
                    videoInfo.address3()
                )
            );
            return new VideoResponse.Detail(
                videoInfo.videoId(),
                videoInfo.influencerName(),
                videoInfo.videoUrl(),
                place
            );
        }

        public static VideoResponse.Detail from(CoolVideo coolVideo) {
            var place = new VideoResponse.PlaceDetail(
                coolVideo.getPlaceId(),
                coolVideo.getPlaceName(),
                new Address(
                    coolVideo.getAddress1(),
                    coolVideo.getAddress2(),
                    coolVideo.getAddress3()
                )
            );
            return new VideoResponse.Detail(
                coolVideo.getVideoId(),
                coolVideo.getInfluencerName(),
                coolVideo.getVideoUrl(),
                place
            );
        }

        public static VideoResponse.Detail from(RecentVideo recentVideo) {
            var place = new VideoResponse.PlaceDetail(
                recentVideo.getPlaceId(),
                recentVideo.getPlaceName(),
                new Address(
                    recentVideo.getAddress1(),
                    recentVideo.getAddress2(),
                    recentVideo.getAddress3()
                )
            );
            return new VideoResponse.Detail(
                recentVideo.getVideoId(),
                recentVideo.getInfluencerName(),
                recentVideo.getVideoUrl(),
                place
            );
        }
    }

    public record PlaceDetail(
        Long placeId,
        String placeName,
        Address address
    ) {
    }

    public record Address(
        String address1,
        String address2,
        String address3
    ) {
    }

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
}
