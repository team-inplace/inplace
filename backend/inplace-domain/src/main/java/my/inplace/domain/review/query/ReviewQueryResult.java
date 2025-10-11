package my.inplace.domain.review.query;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReviewQueryResult {

    public record Simple(
        Long reviewId,
        Boolean likes,
        String comment,
        String userNickname,
        LocalDate createdAt,
        Boolean mine
    ) {

        public Simple(Long reviewId, Boolean likes, String comment, String userNickname, LocalDateTime createdAt, Boolean mine) {
            this(reviewId, likes, comment, userNickname, createdAt.toLocalDate(), mine);
        }
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

        public Detail(Long reviewId, Boolean likes, String comment, LocalDateTime createdAt, Long placeId, String placeName,
            String placeAddress1, String placeAddress2, String placeAddress3) {
            this(reviewId, likes, comment, createdAt.toLocalDate(), placeId, placeName, placeAddress1, placeAddress2, placeAddress3);
        }
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
