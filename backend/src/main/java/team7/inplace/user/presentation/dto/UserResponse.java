package team7.inplace.user.presentation.dto;

import java.time.LocalDate;
import java.util.stream.Collectors;
import team7.inplace.influencer.application.dto.InfluencerInfo;
import team7.inplace.place.application.dto.PlaceInfo;
import team7.inplace.user.application.dto.UserInfo;
import team7.inplace.video.persistence.dto.VideoQueryResult;

public class UserResponse {

    public record Info(
        String nickname,
        String imgUrl
    ) {

        public static Info from(UserInfo.Profile profile) {
            return new Info(profile.nickname(), profile.profileImageUrl());
        }
    }

    public record Review(
        Long reviewId,
        boolean likes,
        String comment,
        LocalDate createdDate,
        ReviewPlace place,
        String videoUrl
    ) {

        public static Review from(
            UserInfo.Review review
        ) {
            return new Review(
                review.reviewId(),
                review.likes(),
                review.comment(),
                review.createdDate(),
                ReviewPlace.from(review.place()),
                review.videoUrl()
            );
        }
    }

    public record ReviewPlace(
        Long placeId,
        String placeName,
        String imgUrl,
        ReviewPlaceAddress address
    ) {

        public static ReviewPlace from(UserInfo.ReviewPlace reviewPlace) {
            return new ReviewPlace(
                reviewPlace.placeId(),
                reviewPlace.placeName(),
                reviewPlace.imgUrl(),
                ReviewPlaceAddress.from(reviewPlace.address())
            );
        }
    }

    public record ReviewPlaceAddress(
        String address1,
        String address2,
        String address3
    ) {

        public static ReviewPlaceAddress from(UserInfo.ReviewPlaceAddress reviewPlaceAddress) {
            return new ReviewPlaceAddress(
                reviewPlaceAddress.address1(),
                reviewPlaceAddress.address2(),
                reviewPlaceAddress.address3()
            );
        }
    }

    public record LikedPlace(
        Long placeId,
        String placeName,
        String imageUrl,
        String influencerName,
        ReviewPlaceAddress address,
        String videoUrl,
        boolean likes
    ) {

        public static LikedPlace from(PlaceInfo.Simple likedPlaceInfo) {
            return new LikedPlace(
                likedPlaceInfo.place().placeId(),
                likedPlaceInfo.place().placeName(),
                "",
                likedPlaceInfo.video()
                    .stream()
                    .map(VideoQueryResult.SimpleVideo::influencerName)
                    .distinct()
                    .collect(Collectors.joining(", ")),
                new ReviewPlaceAddress(
                    likedPlaceInfo.place().address1(),
                    likedPlaceInfo.place().address2(),
                    likedPlaceInfo.place().address3()
                ),
                likedPlaceInfo.video()
                    .stream()
                    .map(VideoQueryResult.SimpleVideo::videoUrl)
                    .findFirst()
                    .orElse(""),
                likedPlaceInfo.place().isLiked()
            );
        }
    }

    public record LikedInfluencer(
        Long influencerId,
        String influencerName,
        String influencerImgUrl,
        String influencerJob,
        boolean likes
    ) {

        public static LikedInfluencer from(InfluencerInfo influencerInfo) {
            return new LikedInfluencer(
                influencerInfo.influencerId(),
                influencerInfo.influencerName(),
                influencerInfo.influencerImgUrl(),
                influencerInfo.influencerJob(),
                influencerInfo.likes()
            );
        }
    }
}
