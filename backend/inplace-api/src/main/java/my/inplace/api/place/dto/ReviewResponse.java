package my.inplace.api.place.dto;

import java.time.LocalDate;
import my.inplace.application.review.dto.ReviewResult;

public class ReviewResponse {

    public record Simple(
        Long reviewId,
        boolean likes,
        String comment,
        String userNickname,
        LocalDate createdDate,
        boolean mine
    ) {

        public static Simple from(ReviewResult.Simple review) {
            return new Simple(
                review.reviewId(),
                review.likes(),
                review.comment(),
                review.userNickname(),
                review.createdAt(),
                review.mine()
            );
        }
    }
}
