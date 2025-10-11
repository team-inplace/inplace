package my.inplace.application.user.command;

import java.util.List;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.user.User;
import my.inplace.domain.user.UserBadge;
import my.inplace.domain.user.query.UserReadRepository;
import my.inplace.application.user.dto.TierConditions;
import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.UserErrorCode;
import my.inplace.infra.user.jpa.TierJpaRepository;
import my.inplace.infra.user.jpa.UserBadgeJpaRepository;
import my.inplace.infra.user.jpa.UserJpaRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserJpaRepository userJpaRepository;
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
    @CachePut(cacheNames = {"receivedCommentCache"}, key = "#p0") // p0 = userId
    public Long addToReceivedCommentByUserId(Long userId, Integer delta) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        return user.getReceivedCommentCount() + delta;
    }

    // TODO: 캐시 적용을 infra에서 처리하도록 관심사 분리
    @CachePut(cacheNames = {"receivedLikeCache"}, key = "#p0") // p0 = userId
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
    
    @Transactional
    public void deleteFcmToken(Long userId) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        
        user.deleteFcmToken();
    }
    
    @Transactional
    public void updateReportPushResent(Long userId, Boolean isResented) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        
        user.updateReportPushResent(isResented);
    }
    
    @Transactional
    public void updateMentionPushResent(Long userId, Boolean isResented) {
        User user = userJpaRepository.findById(userId)
                        .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        
        user.updateMentionPushResent(isResented);
    }
}
