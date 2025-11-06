package my.inplace.application.user.dto;

import java.time.LocalDate;
import my.inplace.domain.review.query.ReviewQueryResult;
import my.inplace.domain.user.query.UserQueryResult;

public class UserResult {

    public record Info(
        String nickname,
        String profileImageUrl,
        String mainBadgeName,
        String mainBadgeImageUrl,
        String tierName,
        String tierImageUrl
    ) {

        public static Info from(UserQueryResult.Info user) {
            return new Info(
                user.nickname(),
                user.imgUrl(),
                user.mainBadgeName(),
                user.mainBadgeImgUrl(),
                user.tierName(),
                user.tierImgUrl()
            );
        }
    }

    public record BadgeWithOwnerShip(
        Long id,
        String name,
        String imgUrl,
        String description,
        Boolean isOwned
    ) {
        public static BadgeWithOwnerShip from(UserQueryResult.BadgeWithOwnerShip badge) {
            return new BadgeWithOwnerShip(
                badge.id(),
                badge.name(),
                badge.imgUrl(),
                badge.description(),
                badge.isOwned()
            );
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
