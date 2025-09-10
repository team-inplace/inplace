package my.inplace.application.review.command;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.ReviewErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.domain.review.Review;
import my.inplace.infra.review.jpa.ReviewJpaRepository;
import my.inplace.security.util.AuthorizationUtil;

@RequiredArgsConstructor
@Service
public class ReviewCommandService {

    private final ReviewJpaRepository reviewJPARepository;

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
