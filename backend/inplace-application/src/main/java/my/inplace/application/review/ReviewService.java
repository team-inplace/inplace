package my.inplace.application.review;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.ReviewErrorCode;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.application.review.dto.ReviewResult;
import my.inplace.infra.review.jpa.ReviewJpaRepository;
import my.inplace.domain.review.query.ReviewQueryResult;
import my.inplace.domain.review.query.ReviewReadRepository;
import my.inplace.security.util.AuthorizationUtil;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewReadRepository reviewReadRepository;
    private final ReviewJpaRepository reviewJPARepository;

    @Transactional(readOnly = true)
    public Page<ReviewResult.Simple> getPlaceReviews(Long placeId, Pageable pageable) {
        Long userId = AuthorizationUtil.getUserIdOrNull();

        return reviewReadRepository
            .findSimpleReviewByUserIdAndPlaceId(placeId, userId, pageable)
            .map(ReviewResult.Simple::from);
    }


    @Transactional(readOnly = true)
    public Page<ReviewQueryResult.Detail> getUserReviews(Long userId, Pageable pageable) {
        return reviewReadRepository.findDetailedReviewByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public ReviewQueryResult.LikeRate getReviewLikeRate(Long reviewId) {
        return reviewReadRepository.countRateByPlaceId(reviewId);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        Review review = reviewJPARepository.findById(reviewId)
            .orElseThrow(() -> InplaceException.of(ReviewErrorCode.NOT_FOUND));

        if (review.isNotOwner(userId)) {
            throw InplaceException.of(ReviewErrorCode.NOT_OWNER);
        }
        reviewJPARepository.delete(review);
    }
}
