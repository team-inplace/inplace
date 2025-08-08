package user.command;

import exception.InplaceException;
import exception.code.UserErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.User;
import user.UserBadge;
import user.dto.TierConditions;
import user.jpa.UserBadgeJpaRepository;
import user.jpa.UserJpaRepository;
import user.jpa.TierJpaRepository;
import user.query.UserReadRepository;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserJpaRepository userJpaRepository;
    private final UserReadRepository userReadRepository;
    private final UserBadgeJpaRepository userBadgeJpaRepository;
    private final TierJpaRepository tierJpaRepository;

    @Transactional
    public void updateNickname(Long userId, String nickname) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        user.updateNickname(nickname);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        List<UserBadge> userBadges = userBadgeJpaRepository.findAllByUserId(userId);
        userBadgeJpaRepository.deleteAllInBatch(userBadges);
        userJpaRepository.delete(user);
    }

    @Transactional
    public void updateBadge(Long userId, Long badgeId) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        user.updateMainBadge(badgeId);
    }

    @Transactional
    public void updateUserTier(Long userId) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        TierConditions tierConditions = TierConditions.of(tierJpaRepository.findAll());
        Long calculatedTierId = tierConditions.getCurrentTierId(
            user.getPostCount(),
            user.getReceivedCommentCount(),
            user.getReceivedLikeCount()
        );

        user.updateTier(calculatedTierId);
    }

    @Transactional
    public void addToPostCount(Long userId, Integer delta) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        user.updatePostCount(user.getPostCount() + delta);
    }

    // TODO: 캐시 적용을 infra에서 처리하도록 관심사 분리
    @CachePut(cacheNames = {"receivedCommentCache"}, key = "#userId")
    public Long addToReceivedCommentByUserId(Long userId, Integer delta) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        return user.getReceivedCommentCount() + delta;
    }

    // TODO: 캐시 적용을 infra에서 처리하도록 관심사 분리
    @CachePut(cacheNames = {"receivedLikeCache"}, key = "#userId")
    public Long addToReceivedLikeByUserId(Long userId, Integer delta) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        return user.getReceivedLikeCount() + delta;
    }

    @Transactional
    public void updateFcmToken(Long userId, String fcmToken) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        user.updateFcmToken(fcmToken);
    }
}
