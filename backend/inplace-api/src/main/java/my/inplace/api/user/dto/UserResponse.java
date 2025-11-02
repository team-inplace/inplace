package my.inplace.api.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import my.inplace.api.post.dto.PostResponse;
import my.inplace.application.influencer.query.dto.InfluencerResult;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import my.inplace.application.place.query.dto.PlaceResult;
import my.inplace.application.post.query.dto.PostResult;
import my.inplace.application.user.dto.UserResult;
import my.inplace.application.video.query.dto.VideoResult;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class UserResponse {
    
    private static String formatCreatedAt(LocalDateTime createdAt) {
        var now = LocalDateTime.now();
        var duration = Duration.between(createdAt, now);
        
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        
        if (minutes < 1) {
            return "방금 전";
        }
        if (minutes < 60) {
            return minutes + "분 전";
        }
        if (hours < 24) {
            return hours + "시간 전";
        }
        if (days < 7) {
            return days + "일 전";
        }
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

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
                likedPlaceResult.videos()
                    .stream()
                    .map(VideoResult.SimpleVideo::influencerName)
                    .distinct()
                    .collect(Collectors.joining(", ")),
                new ReviewPlaceAddress(
                    likedPlaceResult.place().address1(),
                    likedPlaceResult.place().address2(),
                    likedPlaceResult.place().address3()
                ),
                likedPlaceResult.videos()
                    .stream()
                    .map(VideoResult.SimpleVideo::videoUrl)
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

        public static LikedInfluencer from(InfluencerResult.Simple influencerResult) {
            return new LikedInfluencer(
                influencerResult.id(),
                influencerResult.name(),
                influencerResult.imgUrl(),
                influencerResult.job(),
                influencerResult.isLiked()
            );
        }
    }
    
    public record SimplePost(
        Long postId,
        UserResponse.Simple author,
        String title,
        String content,
        @JsonInclude(NON_NULL)
        String imageUrl,
        Boolean selfLike,
        Integer totalLikeCount,
        Integer totalCommentCount,
        Boolean isMine,
        String createdAt
    ) {
        
        public static UserResponse.SimplePost from(PostResult.DetailedPost postResult) {
            return new UserResponse.SimplePost(
                postResult.postId(),
                new UserResponse.Simple(
                    postResult.userNickname(),
                    postResult.userImageUrl(),
                    postResult.tierImageUrl(),
                    postResult.mainBadgeImageUrl()
                ),
                postResult.title(),
                postResult.content(),
                postResult.imageInfos().isEmpty()
                    ? null
                    : postResult.imageInfos().get(0).imageUrl(),
                postResult.selfLike(),
                postResult.totalLikeCount(),
                postResult.totalCommentCount(),
                postResult.isMine(),
                formatCreatedAt(postResult.createdAt())
            );
        }
    }
}
