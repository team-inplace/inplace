package team7.inplace.review.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import team7.inplace.global.baseEntity.BaseEntity;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.ReviewErrorCode;
import team7.inplace.review.domain.Review;
import team7.inplace.review.persistence.ReviewJPARepository;
import team7.inplace.security.util.AuthorizationUtil;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewJPARepository reviewJPARepository;

    @InjectMocks
    private ReviewService reviewService;


    @Test
    @DisplayName("리뷰 삭제 테스트 - 다른 유저의 리뷰 삭제 요청시, NotOwner 예외")
    void deleteReview_NotOwner() throws NoSuchFieldException, IllegalAccessException {
        // given
        MockedStatic<AuthorizationUtil> authorizationUtil = mockStatic(AuthorizationUtil.class);
        Long userId2 = 2L;
        Long reviewId = 1L;

        var review = new Review(1L, 1L, true, "comment"); // userId=1의 리뷰

        Field idField = BaseEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(review, reviewId);

        given(AuthorizationUtil.getUserIdOrThrow()).willReturn(userId2);
        given(reviewJPARepository.findById(reviewId)).willReturn(Optional.of(review));

        // when then
        assertThatThrownBy(() -> reviewService.deleteReview(reviewId))
            .isInstanceOf(InplaceException.class)
            .hasMessage(ReviewErrorCode.NOT_OWNER.getMessage());

        authorizationUtil.close();
    }

    @Test
    @DisplayName("리뷰 삭제 테스트 - 리뷰가 존재하지 않는 경우, NOT_FOUND 예외")
    void deleteReview_NotFound() {
        // given
        MockedStatic<AuthorizationUtil> authorizationUtil = mockStatic(AuthorizationUtil.class);
        Long userId = 1L;
        Long reviewId = 1L;

        given(AuthorizationUtil.getUserIdOrThrow()).willReturn(userId);
        given(reviewJPARepository.findById(reviewId)).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> reviewService.deleteReview(reviewId))
            .isInstanceOf(InplaceException.class)
            .hasMessage(ReviewErrorCode.NOT_FOUND.getMessage());

        authorizationUtil.close();
    }
}
