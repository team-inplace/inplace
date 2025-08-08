package user.query;

import exception.InplaceException;
import exception.code.UserErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.User;
import user.dto.UserResult;
import user.jpa.UserJpaRepository;
import user.query.UserQueryResult.Badge;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserJpaRepository userJpaRepository;
    private final UserReadRepository userReadRepository;

    public Long getUserIdByNickname(String nickname) {
        return userJpaRepository.findIdByNickname(nickname)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
    }

    public UserResult.Simple getUserInfo(Long userId) {
        UserQueryResult.Simple simpleUser = userReadRepository.findUserInfoById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        return UserResult.Simple.from(simpleUser);
    }

    public UserResult.Detail getUserDetail(Long userId) {
        UserQueryResult.Simple simpleUser = userReadRepository.findUserInfoById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        List<Badge> badges = userReadRepository.findAllBadgeByUserId(userId);
        return UserResult.Detail.from(simpleUser, badges);
    }

    public boolean isExistUserName(String userName) {
        return userJpaRepository.existsByUsername(userName);
    }

    public String getFcmTokenByUser(Long userId) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        return user.getFcmToken();
    }
}
