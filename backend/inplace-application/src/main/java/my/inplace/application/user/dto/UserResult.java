package my.inplace.application.user.dto;

import java.time.LocalDate;
import java.util.List;
import my.inplace.domain.review.query.ReviewQueryResult;
import my.inplace.domain.user.query.UserQueryResult;

public class UserResult {

    public record Simple(
        String nickname,
        String profileImageUrl,
        String mainBadgeName,
        String mainBadgeImageUrl,
        String tierName,
        String tierImageUrl
    ) {

        public static Simple from(UserQueryResult.Simple user) {
            return new Simple(
                user.nickname(),
                user.imgUrl(),
                user.mainBadgeName(),
                user.mainBadgeImgUrl(),
                user.tierName(),
                user.tierImgUrl()
            );
        }
    }

    public record Detail(
        String nickname,
        String profileImageUrl,
        String tierName,
        String tierImageUrl,
        List<Badge> badges
    ) {

        public static Detail from(
            UserQueryResult.Simple user, List<UserQueryResult.Badge> badgeQueryResults) {
            List<Badge> badges = badgeQueryResults.stream().map(Badge::from).toList();
            return new Detail(
                user.nickname(),
                user.imgUrl(),
                user.tierName(),
                user.tierImgUrl(),
                badges
            );
        }
    }

    public record Badge(
        Long id,
        String name,
        String img_url,
        String condition
    ) {

        public static Badge from(UserQueryResult.Badge badge) {
            return new Badge(badge.id(), badge.name(), badge.imgUrl(), badge.condition());
        }
    }

    public record Review(
        Long reviewId,
        boolean likes,
        String comment,
        LocalDate createdDate,
        UserResult.ReviewPlace place,
        String videoUrl
    ) {

        public static UserResult.Review from(ReviewQueryResult.Detail review, String videoUrl) {
            var reviewPlaceAddress = new UserResult.ReviewPlaceAddress(
                review.placeAddress1(),
                review.placeAddress2(),
                review.placeAddress3()
            );
            var reviewPlace = new UserResult.ReviewPlace(
                review.placeId(),
                review.placeName(),
                "",
                reviewPlaceAddress
            );
            return new UserResult.Review(
                review.reviewId(),
                review.likes(),
                review.comment(),
                review.createdAt(),
                reviewPlace,
                videoUrl
            );
        }
    }

    public record ReviewPlace(
        Long placeId,
        String placeName,
        String imgUrl,
        UserResult.ReviewPlaceAddress address
    ) {

    }

    public record ReviewPlaceAddress(
        String address1,
        String address2,
        String address3
    ) {

    }
}
