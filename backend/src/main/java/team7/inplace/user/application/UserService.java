package team7.inplace.user.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.UserErrorCode;
import team7.inplace.user.application.dto.UserCommand;
import team7.inplace.user.application.dto.UserCommand.Info;
import team7.inplace.user.application.dto.UserInfo;
import team7.inplace.user.domain.User;
import team7.inplace.user.persistence.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserCommand.Info registerUser(UserCommand.Create userCreate) {
        User user = userCreate.toEntity();
        userRepository.save(user);
        return UserCommand.Info.of(user);
    }

    @Transactional(readOnly = true)
    public UserCommand.Info getUserByUsername(String username) {
        return UserCommand.Info.of(userRepository.findByUsername(username)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND)));
    }

    @Transactional
    public Optional<Info> findUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.map(Info::of);
    }

    @Transactional
    public void updateNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        user.updateNickname(nickname);
    }

    @Transactional(readOnly = true)
    public UserInfo getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        return UserInfo.from(user);
    }

    @Transactional()
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        userRepository.delete(user);
    }

    @Transactional
    public void updateProfileImageUrl(Long userId, String profileImageUrl) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));

        user.updateProfileImageUrl(profileImageUrl);
    }
}
