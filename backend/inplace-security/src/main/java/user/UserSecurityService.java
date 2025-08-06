package user;

import exception.InplaceException;
import exception.code.UserErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.jpa.UserJpaRepository;
import user.query.UserReadRepository;

@Service
@RequiredArgsConstructor
public class UserSecurityService {

    private final UserJpaRepository userJpaRepository;
    private final UserReadRepository userReadRepository;

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
