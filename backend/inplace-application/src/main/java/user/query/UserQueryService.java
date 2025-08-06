package user.query;

import exception.InplaceException;
import exception.code.UserErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.User;
import user.dto.UserCommand.Info;
import user.dto.UserResult.Detail;
import user.dto.UserResult.Simple;
import user.jpa.UserJpaRepository;
import user.query.UserQueryResult.Badge;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserJpaRepository userJpaRepository;
    private final UserReadRepository userReadRepository;

    @Transactional(readOnly = true)
    public Info getUserByUsername(String username) {
        return Info.of(userJpaRepository.findByUsername(username)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND)));
    }

    @Transactional(readOnly = true)
    public Simple getUserInfo(Long userId) {
        UserQueryResult.Simple simpleUser = userReadRepository.findUserInfoById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        return Simple.from(simpleUser);
    }

    @Transactional
    public Detail getUserDetail(Long userId) {
        UserQueryResult.Simple simpleUser = userReadRepository.findUserInfoById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        List<Badge> badges = userReadRepository.findAllBadgeByUserId(userId);
        return Detail.from(simpleUser, badges);
    }

    @Transactional(readOnly = true)
    public boolean isExistUserName(String userName) {
        return userJpaRepository.existsByUsername(userName);
    }

    @Transactional(readOnly = true)
    public String getFcmTokenByUser(Long userId) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        return user.getFcmToken();
    }
}
