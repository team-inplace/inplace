package team7.inplace.influencer.presentation.dto;

import team7.inplace.influencer.application.dto.InfluencerInfo;
import team7.inplace.influencer.application.dto.InfluencerNameInfo;
import team7.inplace.influencer.persistence.dto.InfluencerQueryResult;
import team7.inplace.video.persistence.dto.VideoQueryResult;
import team7.inplace.video.presentation.dto.VideoResponse;

public class InfluencerResponse {

    //TODO: 클래스 이름 변경해야함
    public record Info(
        Long influencerId,
        String influencerName,
        String influencerImgUrl,
        String influencerJob,
        boolean likes
    ) {

        public static InfluencerResponse.Info from(InfluencerInfo influencerInfo) {
            return new InfluencerResponse.Info(
                influencerInfo.influencerId(),
                influencerInfo.influencerName(),
                influencerInfo.influencerImgUrl(),
                influencerInfo.influencerJob(),
                influencerInfo.likes()
            );
        }

        public static InfluencerResponse.Info from(InfluencerQueryResult.Simple influencerInfo) {
            return new InfluencerResponse.Info(
                influencerInfo.id(),
                influencerInfo.name(),
                influencerInfo.imgUrl(),
                influencerInfo.job(),
                influencerInfo.isLiked()
            );
        }
    }

    public record Name(
        String influencerName
    ) {

        public static Name from(InfluencerNameInfo influencerInfo) {
            return new Name(influencerInfo.name());
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

        public static Detail from(InfluencerQueryResult.Detail influencerInfo) {
            return new Detail(
                influencerInfo.id(),
                influencerInfo.name(),
                influencerInfo.imgUrl(),
                influencerInfo.job(),
                influencerInfo.isLiked(),
                influencerInfo.follower(),
                influencerInfo.placeCount()
            );
        }
    }

    public record Video(
        Long videoId,
        String influencerName,
        String videoUrl,
        InfluencerResponse.Place place
    ) {

        public static InfluencerResponse.Video from(VideoQueryResult.DetailedVideo videoInfo) {
            var place = new InfluencerResponse.Place(
                videoInfo.placeId(),
                videoInfo.placeName(),
                new Address(
                        videoInfo.address1(),
                        videoInfo.address2(),
                        videoInfo.address3()
                )
            );
            return new InfluencerResponse.Video(
                videoInfo.videoId(),
                videoInfo.influencerName(),
                videoInfo.videoUrl(),
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
