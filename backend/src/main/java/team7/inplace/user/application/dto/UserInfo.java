package team7.inplace.user.application.dto;

import team7.inplace.review.persistence.dto.ReviewQueryResult;
import team7.inplace.user.domain.User;

import java.time.LocalDate;

public class UserInfo{
    public record Profile(
            String nickname,
            String profileImageUrl
    ) {
        public static Profile from(User user) {
            return new Profile(user.getNickname(), user.getProfileImageUrl());
        }
    }

    public record Review(
            Long reviewId,
            boolean likes,
            String comment,
            LocalDate createdDate,
            UserInfo.ReviewPlace place,
            String videoUrl
    ) {

        public static UserInfo.Review from(ReviewQueryResult.Detail review, String videoUrl) {
            var reviewPlaceAddress = new UserInfo.ReviewPlaceAddress(
                    review.placeAddress1(),
                    review.placeAddress2(),
                    review.placeAddress3()
            );
            var reviewPlace = new UserInfo.ReviewPlace(
                    review.placeId(),
                    review.placeName(),
                    "",
                    reviewPlaceAddress
            );
            return new UserInfo.Review(
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
            UserInfo.ReviewPlaceAddress address
    ) {

    }

    public record ReviewPlaceAddress(
            String address1,
            String address2,
            String address3
    ) {

    }
}
