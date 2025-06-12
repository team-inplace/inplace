package team7.inplace.user.presentation.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import team7.inplace.influencer.application.dto.InfluencerInfo;
import team7.inplace.place.application.dto.PlaceInfo;
import team7.inplace.user.application.dto.UserInfo;
import team7.inplace.user.application.dto.UserInfo.Badge;
import team7.inplace.user.application.dto.UserInfo.Detail;
import team7.inplace.user.application.dto.UserInfo.Simple;
import team7.inplace.video.persistence.dto.VideoQueryResult;

public class UserResponse {

    public record Simple(
        String nickname,
        String imageUrl,
        String tierImageUrl,
        String mainBadgeImageUrl
    ) {

        public static Simple from(UserInfo.Simple simple) {
            return new Simple(
                simple.nickname(),
                simple.profileImageUrl(),
                simple.tierImageUrl(),
                simple.mainBadgeImageUrl()
            );
        }
    }

    public record Detail(
        String nickname,
        String imgUrl,
        Tier tier,
        List<Badge> badges
    ) {

        public static Detail from(UserInfo.Detail detail) {
            return new Detail(
                detail.nickname(),
                detail.profileImageUrl(),
                new Tier(detail.tierName(), detail.tierImageUrl()),
                detail.badges().stream().map(Badge::from).toList()
            );
        }
    }

    public record Badge(
        Long id,
        String name,
        String imgUrl
    ) {
        public static Badge from(UserInfo.Badge badge) {
            return new Badge(
                badge.id(),
                badge.name(),
                badge.img_url()
            );
        }
    }

    public record Tier(
        String name,
        String imgUrl
    ) {
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
