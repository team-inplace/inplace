package team7.inplace.placeMessage.application;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.UserErrorCode;
import team7.inplace.placeMessage.domain.UserReviewUuid;
import team7.inplace.placeMessage.persistence.UserReviewUuidRepository;
import team7.inplace.user.domain.User;
import team7.inplace.user.persistence.UserRepository;

@Service
@RequiredArgsConstructor
public class UserReviewUuidService {

    private final UserReviewUuidRepository userReviewUuidRepository;
    private final UserRepository userRepository;

    @Transactional
    public String generateReviewUuid(Long userId, Long placeId) {
        Optional<UserReviewUuid> existingUuid = userReviewUuidRepository.findByUserIdAndPlaceId(
            userId, placeId);

        if (existingUuid.isPresent()) {
            return existingUuid.get().getUuid();
        }

        // uuid 새로 생성
        String uuid = UUID.randomUUID().toString();

        User user = userRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        UserReviewUuid reviewLink = new UserReviewUuid(user, placeId, uuid);
        userReviewUuidRepository.save(reviewLink);

        return uuid;

    }
}
