package user.dto;

import influencer.dto.InfluencerResult;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import place.dto.PlaceResult;

public class UserResponse {

    public record Simple(
        String nickname,
        String imageUrl,
        String tierImageUrl,
        String mainBadgeImageUrl
    ) {

        public static Simple from(UserResult.Simple simple) {
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

        public static Detail from(UserResult.Detail detail) {
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

        public static Badge from(UserResult.Badge badge) {
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
            UserResult.Review review
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

        public static ReviewPlace from(UserResult.ReviewPlace reviewPlace) {
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

        public static ReviewPlaceAddress from(UserResult.ReviewPlaceAddress reviewPlaceAddress) {
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

        public static LikedPlace from(PlaceResult.Simple likedPlaceResult) {
            return new LikedPlace(
                likedPlaceResult.place().placeId(),
                likedPlaceResult.place().placeName(),
                "",
                likedPlaceResult.video()
                    .stream()
                    .map(VideoQueryResult.SimpleVideo::influencerName)
                    .distinct()
                    .collect(Collectors.joining(", ")),
                new ReviewPlaceAddress(
                    likedPlaceResult.place().address1(),
                    likedPlaceResult.place().address2(),
                    likedPlaceResult.place().address3()
                ),
                likedPlaceResult.video()
                    .stream()
                    .map(VideoQueryResult.SimpleVideo::videoUrl)
                    .findFirst()
                    .orElse(""),
                likedPlaceResult.place().isLiked()
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

        public static LikedInfluencer from(InfluencerResult influencerResult) {
            return new LikedInfluencer(
                influencerResult.influencerId(),
                influencerResult.influencerName(),
                influencerResult.influencerImgUrl(),
                influencerResult.influencerJob(),
                influencerResult.likes()
            );
        }
    }
}
