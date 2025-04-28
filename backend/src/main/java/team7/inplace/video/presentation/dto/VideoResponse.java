package team7.inplace.video.presentation.dto;

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
                String.join(" ", videoInfo.address1(), videoInfo.address2(), videoInfo.address3())
            );
            return new VideoResponse.Detail(
                videoInfo.videoId(),
                videoInfo.influencerName(),
                videoInfo.videoUrl(),
                place
            );
        }
    }

    public record PlaceDetail(
        Long placeId,
        String placeName,
        String placeAddress
    ) {
    }

}