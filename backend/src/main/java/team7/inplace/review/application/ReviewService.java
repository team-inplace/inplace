package team7.inplace.review.application;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;
import team7.inplace.global.exception.code.PlaceErrorCode;
import team7.inplace.global.exception.code.ReviewErrorCode;
import team7.inplace.place.domain.Place;
import team7.inplace.place.persistence.PlaceRepository;
import team7.inplace.placeMessage.persistence.UserReviewUuidRepository;
import team7.inplace.review.application.dto.MyReviewInfo;
import team7.inplace.review.application.dto.ReviewCommand;
import team7.inplace.review.application.dto.ReviewInfo;
import team7.inplace.review.domain.Review;
import team7.inplace.review.persistence.ReviewRepository;
import team7.inplace.security.application.CurrentUserProvider;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.user.domain.User;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;
    private final CurrentUserProvider currentUserProvider;
    private final UserReviewUuidRepository userReviewUuidRepository;

    @Transactional
    public void createReview(Long placeId, ReviewCommand command, String uuid) {
        User user = userReviewUuidRepository.findUserByUuidAndPlaceId(uuid, placeId)
            .orElseThrow(() -> InplaceException.of(ReviewErrorCode.INVALID_UUID));
        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));

        if (reviewRepository.existsByUserIdAndPlaceId(user.getId(), placeId)) {
            throw InplaceException.of(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
        }
        Review review = ReviewCommand.toEntity(user, place, command);
        reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public Page<ReviewInfo> getReviews(Long placeId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findByPlaceId(placeId, pageable);
        // 로그인 안된 경우 mine을 모두 false로 설정
        if (AuthorizationUtil.isNotLoginUser()) {
            return reviewPage.map(review -> ReviewInfo.from(review, false));
        }

        Long userId = AuthorizationUtil.getUserId();

        return reviewPage.map(review -> {
            boolean isMine = Objects.nonNull(userId) && review.getUser().getId().equals(userId);
            return ReviewInfo.from(review, isMine);
        });
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        if (AuthorizationUtil.isNotLoginUser()) {
            throw InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY);
        }

        Long userId = AuthorizationUtil.getUserId();
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> InplaceException.of(ReviewErrorCode.NOT_FOUND));

        if (!review.getUser().getId().equals(userId)) {
            throw InplaceException.of(ReviewErrorCode.NOT_OWNER);
        }

        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public Page<MyReviewInfo> getMyReviews(Long userId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findByUserIdWithPlace(userId, pageable);

        return reviewPage.map(MyReviewInfo::from);
    }
}
