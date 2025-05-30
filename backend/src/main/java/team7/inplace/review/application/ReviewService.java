package team7.inplace.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.ReviewErrorCode;
import team7.inplace.review.domain.Review;
import team7.inplace.review.persistence.ReviewJPARepository;
import team7.inplace.review.persistence.ReviewReadRepository;
import team7.inplace.review.persistence.dto.ReviewQueryResult;
import team7.inplace.security.util.AuthorizationUtil;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewReadRepository reviewReadRepository;
    private final ReviewJPARepository reviewJPARepository;

    @Transactional(readOnly = true)
    public Page<ReviewQueryResult.Simple> getPlaceReviews(Long placeId, Pageable pageable) {
        Long userId = AuthorizationUtil.getUserIdOrNull();

        return reviewReadRepository
            .findSimpleReviewByUserIdAndPlaceId(placeId, userId, pageable);
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
