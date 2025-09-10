package my.inplace.application.review.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import my.inplace.domain.place.query.PlaceQueryResult;
import my.inplace.domain.review.Review;
import my.inplace.domain.review.query.ReviewQueryResult;
import my.inplace.domain.user.User;
import my.inplace.domain.video.query.VideoQueryResult.SimpleVideo;

public class ReviewResult {

    public record Detail(
        Long id,
        boolean likes,
        String comment,
        Long userId,
        LocalDate createdDate,
        boolean mine
    ) {

        public static Detail from(Review review, boolean isMine) {
            return new Detail(
                review.getId(),
                review.getIsLiked(),
                review.getComment(),
                review.getUserId(),
                review.getCreatedDate(),
                isMine
            );
        }
    }

    public record Simple(
        Long reviewId,
        Boolean likes,
        String comment,
        String userNickname,
        LocalDate createdAt,
        Boolean mine
    ) {
        public static Simple from(ReviewQueryResult.Simple review) {
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

    public record LikeRate(
        Long likes,
        Long dislikes
    ) {

        public static LikeRate from(ReviewQueryResult.LikeRate review) {
            return new LikeRate(
                review.likes(),
                review.dislikes()
            );
        }
    }

    public record Invitation(
        String placeName,
        String placeAddress,
        String influencerName,
        String userNickname
    ) {

        public static Invitation from(
            PlaceQueryResult.DetailedPlace place,
            List<SimpleVideo> video,
            User user
        ) {
            return new Invitation(
                place.placeName(),
                place.address1() + " " + place.address2() + " " + place.address3(),
                video.stream().map(SimpleVideo::influencerName).distinct()
                    .collect(Collectors.joining(", ")),
                user.getNickname()
            );
        }
    }
}
