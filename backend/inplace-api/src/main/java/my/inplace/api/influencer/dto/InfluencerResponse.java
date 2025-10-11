package my.inplace.api.influencer.dto;

import my.inplace.application.influencer.query.dto.InfluencerResult;
import my.inplace.application.video.query.dto.VideoResult;

public class InfluencerResponse {

    public record Simple(
        Long influencerId,
        String influencerName,
        String influencerImgUrl,
        String influencerJob,
        boolean likes
    ) {

        public static InfluencerResponse.Simple from(InfluencerResult.Simple influencerResult) {
            return new InfluencerResponse.Simple(
                influencerResult.id(),
                influencerResult.name(),
                influencerResult.imgUrl(),
                influencerResult.job(),
                influencerResult.isLiked()
            );
        }
    }

    public record Name(
        String influencerName
    ) {

        public static Name from(InfluencerResult.Name influencerResult) {
            return new Name(influencerResult.name());
        }
    }

    public record Detail(
        Long influencerId,
        String influencerName,
        String influencerImgUrl,
        String influencerJob,
        Boolean likes,
        Long follower,
        Long placeCount
    ) {

        public static Detail from(InfluencerResult.Detail influencerResult) {
            return new Detail(
                influencerResult.id(),
                influencerResult.name(),
                influencerResult.imgUrl(),
                influencerResult.job(),
                influencerResult.isLiked(),
                influencerResult.follower(),
                influencerResult.placeCount()
            );
        }
    }

    public record Video(
        Long videoId,
        String influencerName,
        String videoUrl,
        InfluencerResponse.Place place
    ) {

        public static InfluencerResponse.Video from(VideoResult.DetailedVideo videoResult) {
            var place = new InfluencerResponse.Place(
                videoResult.placeId(),
                videoResult.placeName(),
                new Address(
                    videoResult.address1(),
                    videoResult.address2(),
                    videoResult.address3()
                )
            );
            return new InfluencerResponse.Video(
                videoResult.videoId(),
                videoResult.influencerName(),
                videoResult.videoUrl(),
                place
            );
        }
    }

    public record Place(
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
}
