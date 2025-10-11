package my.inplace.security.application;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import my.inplace.security.util.AuthorizationUtil;
import my.inplace.domain.user.User;
import my.inplace.infra.user.jpa.UserJpaRepository;

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
