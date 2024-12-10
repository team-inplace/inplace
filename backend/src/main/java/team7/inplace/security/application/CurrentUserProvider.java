package team7.inplace.security.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;
import team7.inplace.global.exception.code.UserErrorCode;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.user.domain.User;
import team7.inplace.user.persistence.UserRepository;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Long userId = AuthorizationUtil.getUserId();
        if (userId == null) {
            throw InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY);
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
    }
}
