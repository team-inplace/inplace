package user;

import exception.InplaceException;
import exception.code.UserErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.dto.TierConditions;
import user.dto.UserCommand.Create;
import user.dto.UserCommand.Info;
import user.dto.UserResult.Detail;
import user.dto.UserResult.Simple;
import user.jpa.UserBadgeJpaRepository;
import user.jpa.UserJpaRepository;
import user.jpa.UserTierJpaRepository;
import user.query.UserQueryResult;
import user.query.UserQueryResult.Badge;
import user.query.UserReadRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final UserReadRepository userReadRepository;
    private final UserBadgeJpaRepository userBadgeJpaRepository;
    private final UserTierJpaRepository userTierJpaRepository;

    @Transactional
    public Info registerUser(Create userCreate) {
        User user = userCreate.toEntity();
        userJpaRepository.save(user);
        return Info.of(user);
    }

    @Transactional(readOnly = true)
    public Info getUserByUsername(String username) {
        return Info.of(userJpaRepository.findByUsername(username)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND)));
    }

    @Transactional
    public Optional<Info> findUserByUsername(String username) {
        Optional<User> userOptional = userJpaRepository.findByUsername(username);
        return userOptional.map(Info::of);
    }

    @Transactional
    public void updateNickname(Long userId, String nickname) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        user.updateNickname(nickname);
    }

    @Transactional(readOnly = true)
    public Simple getUserInfo(Long userId) {
        UserQueryResult.Simple simpleUser = userReadRepository.findUserInfoById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        return Simple.from(simpleUser);
    }

    @Transactional()
    public void deleteUser(Long userId) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        List<UserBadge> userBadges = userBadgeJpaRepository.findAllByUserId(userId);
        userBadgeJpaRepository.deleteAllInBatch(userBadges);
        userJpaRepository.delete(user);
    }

    @Transactional
    public void updateProfileImageUrl(Long userId, String profileImageUrl) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        user.updateProfileImageUrl(profileImageUrl);
    }

    @Transactional
    public Detail getUserDetail(Long userId) {
        UserQueryResult.Simple simpleUser = userReadRepository.findUserInfoById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        List<Badge> badges = userReadRepository.findAllBadgeByUserId(userId);
        return Detail.from(simpleUser, badges);
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

        TierConditions tierConditions = TierConditions.of(userTierJpaRepository.findAll());
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

    @Transactional(readOnly = true)
    public boolean isExistUserName(String userName) {
        return userJpaRepository.existsByUsername(userName);
    }

    @Transactional
    public void updateFcmToken(Long userId, String fcmToken) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        user.updateFcmToken(fcmToken);
    }

    @Transactional(readOnly = true)
    public String getFcmTokenByUser(Long userId) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        return user.getFcmToken();
    }
}
