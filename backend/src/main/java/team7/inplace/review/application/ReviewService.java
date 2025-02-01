package team7.inplace.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;
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
        Long userId = AuthorizationUtil.getUserId();

        Page<ReviewQueryResult.Simple> reviewResults = reviewReadRepository
            .findSimpleReviewByUserIdAndPlaceId(placeId, userId, pageable);
        return reviewResults;
    }


    @Transactional(readOnly = true)
    public Page<ReviewQueryResult.Detail> getUserReviews(Long userId, Pageable pageable) {
        Page<ReviewQueryResult.Detail> reviewPage =
            reviewReadRepository.findDetailedReviewByUserId(userId, pageable);

        return reviewPage;
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Long userId = AuthorizationUtil.getUserId();
        if (userId == null) {
            throw InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY);
        }

        Review review = reviewJPARepository.findById(reviewId)
            .orElseThrow(() -> InplaceException.of(ReviewErrorCode.NOT_FOUND));

        if (review.isNotOwner(userId)) {
            throw InplaceException.of(ReviewErrorCode.NOT_OWNER);
        }
        reviewJPARepository.delete(review);
    }
}
