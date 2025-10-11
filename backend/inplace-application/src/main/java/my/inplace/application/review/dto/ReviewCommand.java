package my.inplace.application.review.dto;

import my.inplace.domain.review.Review;

public class ReviewCommand {

    public record create(
        String uuid,
        boolean likes,
        String comments
    ) {

        public Review toEntityFrom(Long userId, Long placeId) {
            return new Review(
                userId,
                placeId,
                likes,
                comments
            );
        }
    }

}
