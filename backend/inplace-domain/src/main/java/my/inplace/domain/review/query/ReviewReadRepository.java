package my.inplace.domain.review.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewReadRepository {

    Page<ReviewQueryResult.Detail> findDetailedReviewByUserId(Long userId, Pageable pageable);

    Page<ReviewQueryResult.Simple> findSimpleReviewByUserIdAndPlaceId(
        Long placeId, Long userId, Pageable pageable);

    ReviewQueryResult.LikeRate countRateByPlaceId(Long placeId);
}
