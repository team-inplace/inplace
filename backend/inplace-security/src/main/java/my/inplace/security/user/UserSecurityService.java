package my.inplace.security.user;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.UserErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.security.user.dto.UserSecurityCommand;
import my.inplace.security.user.dto.UserSecurityResult;
import my.inplace.infra.user.jpa.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class UserSecurityService {

    private final UserJpaRepository userJpaRepository;

    @Transactional(readOnly = true)
    public Optional<UserSecurityResult.Info> findUserByUsername(String username) {
        return userJpaRepository.findByUsername(username)
            .map(UserSecurityResult.Info::from);
    }

    @Transactional
    public UserSecurityResult.Info registerUser(UserSecurityCommand.Create userCreate) {
        User user = userCreate.toEntity();
        userJpaRepository.save(user);
        return UserSecurityResult.Info.from(user);
    }

    @Transactional
    public void updateProfileImageUrl(Long userId, String profileImageUrl) {
        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        user.updateProfileImageUrl(profileImageUrl);
    }
}
