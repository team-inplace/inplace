package application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.UserErrorCode;
import util.AuthorizationUtil;
import user.User;
import user.jpa.UserJpaRepository;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserJpaRepository userJpaRepository;

    public User getCurrentUser() {
        Long userId = AuthorizationUtil.getUserIdOrThrow();

        return userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
    }
}
