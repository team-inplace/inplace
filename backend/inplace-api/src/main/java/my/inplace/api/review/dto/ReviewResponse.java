package my.inplace.api.review.dto;

import my.inplace.application.review.dto.ReviewResult;

public class ReviewResponse {

    public record Invitation(
        String placeName,
        String placeAddress,
        String placeImgUrl,
        String influencerName,
        String userNickname
    ) {

        public static Invitation from(ReviewResult.Invitation reviewResult) {
            return new Invitation(
                reviewResult.placeName(),
                reviewResult.placeAddress(),
                "",
                reviewResult.influencerName(),
                reviewResult.userNickname()
            );
        }
    }
}
