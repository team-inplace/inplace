package team7.inplace.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.liked.likedInfluencer.persistent.FavoriteInfluencerRepository;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.UserErrorCode;
import team7.inplace.security.application.CurrentUserProvider;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.user.application.dto.UserCommand;
import team7.inplace.user.application.dto.UserCommand.Info;
import team7.inplace.user.application.dto.UserInfo;
import team7.inplace.user.domain.User;
import team7.inplace.user.persistence.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FavoriteInfluencerRepository favoriteInfluencerRepository;
    private final CurrentUserProvider currentUserProvider;

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

    @Transactional(readOnly = true)
    public List<Long> getInfluencerIdsByUserId(Long userId) {
        Set<Long> likes = favoriteInfluencerRepository.findLikedInfluencerIdsByUserId(userId);
        return likes.stream().toList();
    }

    @Transactional
    public void updateNickname(String nickname) {
        User user = userRepository.findByUsername(AuthorizationUtil.getUsername()).orElseThrow(
                () -> InplaceException.of(UserErrorCode.NOT_FOUND)
        );

        user.updateInfo(nickname);
    }

    @Transactional(readOnly = true)
    public UserInfo getUserInfo() {
        User user = currentUserProvider.getCurrentUser();
        return UserInfo.from(user);
    }
}
