package team7.inplace.user.application;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.UserErrorCode;
import team7.inplace.user.application.dto.UserCommand;
import team7.inplace.user.application.dto.UserCommand.Info;
import team7.inplace.user.application.dto.UserInfo;
import team7.inplace.user.application.dto.UserInfo.Detail;
import team7.inplace.user.domain.User;
import team7.inplace.user.domain.UserBadge;
import team7.inplace.user.persistence.UserBadgeJpaRepository;
import team7.inplace.user.persistence.UserReadRepository;
import team7.inplace.user.persistence.UserJpaRepository;
import team7.inplace.user.persistence.dto.UserQueryResult;
import team7.inplace.user.persistence.dto.UserQueryResult.Badge;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final UserReadRepository userReadRepository;
    private final UserBadgeJpaRepository userBadgeJpaRepository;

    @Transactional
    public UserCommand.Info registerUser(UserCommand.Create userCreate) {
        User user = userCreate.toEntity();
        userJpaRepository.save(user);
        return UserCommand.Info.of(user);
    }

    @Transactional(readOnly = true)
    public UserCommand.Info getUserByUsername(String username) {
        return UserCommand.Info.of(userJpaRepository.findByUsername(username)
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
    public UserInfo.Simple getUserInfo(Long userId) {
        UserQueryResult.Simple simpleUser = userReadRepository.findUserInfoById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        return UserInfo.Simple.from(simpleUser);
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
}
