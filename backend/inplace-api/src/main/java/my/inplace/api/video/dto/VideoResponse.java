package my.inplace.api.video.dto;

import my.inplace.application.video.query.dto.VideoResult;

// Video 엔티티의 Controller Response 정보 전달을 담당하는 클래스
public class VideoResponse {

    public record Simple(
        Long videoId,
        String videoUrl,
        VideoResponse.Place place
    ) {

        public static VideoResponse.Simple from(VideoResult.SimpleVideo videoResult) {
            var place = new VideoResponse.Place(
                videoResult.placeId(),
                videoResult.placeName()
            );
            return new VideoResponse.Simple(
                videoResult.videoId(),
                videoResult.videoUrl(),
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

        public static VideoResponse.Detail from(VideoResult.DetailedVideo videoResult) {
            var place = new VideoResponse.PlaceDetail(
                videoResult.placeId(),
                videoResult.placeName(),
                new Address(
                    videoResult.address1(),
                    videoResult.address2(),
                    videoResult.address3()
                )
            );
            return new VideoResponse.Detail(
                videoResult.videoId(),
                videoResult.influencerName(),
                videoResult.videoUrl(),
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

        public static Admin from(VideoResult.Admin adminVideo) {
            return new Admin(
                adminVideo.id(),
                adminVideo.uuid(),
                adminVideo.registered()
            );
        }
    }
}
