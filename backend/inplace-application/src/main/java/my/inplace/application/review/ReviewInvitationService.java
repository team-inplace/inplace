package my.inplace.application.review;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import my.inplace.application.review.dto.ReviewCommand;
import my.inplace.application.review.dto.ReviewResult;
import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.ReviewErrorCode;
import my.inplace.domain.place.query.PlaceReadRepository;
import my.inplace.domain.review.ReviewInvitation;
import my.inplace.domain.video.query.VideoReadRepository;
import my.inplace.infra.review.ReviewInvitationRedisRepository;
import my.inplace.infra.review.jpa.ReviewJpaRepository;
import my.inplace.infra.user.jpa.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewInvitationService {

    private final ReviewInvitationRedisRepository reviewInvitationRedisRepository;
    private final ReviewJpaRepository reviewJPARepository;
    private final UserJpaRepository userJpaRepository;
    private final PlaceReadRepository placeReadRepository;
    private final VideoReadRepository videoReadRepository;

    @Transactional
    public String generateReviewUuid(Long userId, Long placeId) {
        //리뷰가 작성되어 있다면 null을 반환
        var reviewed = reviewJPARepository.existsByUserIdAndPlaceId(userId, placeId);
        if (reviewed) {
            return null;
        }

        //이미 초대장이 존재한다면 해당 초대장의 uuid를 반환
        var existingInvitation = reviewInvitationRedisRepository.findUUIDByUserIdAndPlaceId(
            userId, placeId);
        if (existingInvitation.isPresent()) {
            return existingInvitation.get();
        }

        // uuid 새로 생성
        String newInvitationId = UUID.randomUUID().toString();
        ReviewInvitation newInvitation = new ReviewInvitation(newInvitationId, userId, placeId);
        reviewInvitationRedisRepository.save(newInvitationId, newInvitation);

        return newInvitationId;
    }

    @Transactional
    public void saveReview(ReviewCommand.create command) {
        var reviewInvitation = reviewInvitationRedisRepository.get(command.uuid())
            .orElseThrow(() -> InplaceException.of(ReviewErrorCode.INVALID_UUID));

        var review = command.toEntityFrom(reviewInvitation.getUserId(),
            reviewInvitation.getPlaceId());
        reviewJPARepository.save(review);
        reviewInvitationRedisRepository.delete(command.uuid());
    }

    @Transactional(readOnly = true)
    public ReviewResult.Invitation getReviewInvitation(String invitationId) {
        var invitation = reviewInvitationRedisRepository.get(invitationId)
            .orElseThrow(() -> InplaceException.of(ReviewErrorCode.INVALID_UUID));

        var place = placeReadRepository.findDetailedPlaceById(invitation.getPlaceId(),
                invitation.getUserId())
            .orElseThrow(() -> InplaceException.of(ReviewErrorCode.INVALID_PLACE_ID));
        var user = userJpaRepository.findById(invitation.getUserId())
            .orElseThrow(() -> InplaceException.of(ReviewErrorCode.INVALID_USER_ID));
        var videos = videoReadRepository.findSimpleVideosByPlaceId(invitation.getPlaceId());

        return ReviewResult.Invitation.from(place, videos, user);
    }
}
