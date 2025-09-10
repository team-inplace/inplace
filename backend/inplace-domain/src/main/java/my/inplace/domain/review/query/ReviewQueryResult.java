package my.inplace.domain.review.query;

import java.time.LocalDate;

public class ReviewQueryResult {

    public record Simple(
        Long reviewId,
        Boolean likes,
        String comment,
        String userNickname,
        LocalDate createdAt,
        Boolean mine
    ) {

    }

    public record Detail(
        Long reviewId,
        Boolean likes,
        String comment,
        LocalDate createdAt,
        Long placeId,
        String placeName,
        String placeAddress1,
        String placeAddress2,
        String placeAddress3
    ) {

    }

    public record LikeRate(
        Long likes,
        Long dislikes
    ) {

        public LikeRate(int likes, int dislikes) {
            this((long) likes, (long) dislikes);
        }
    }
}
