package user.dto;

import team7.inplace.security.application.dto.KakaoOAuthResponse;
import user.Role;
import user.User;
import user.UserType;


public class UserCommand {

    public record Create(
        String username,
        String nickname,
        String profileImageUrl,
        UserType userType,
        Role role
    ) {

        public static UserCommand.Create of(KakaoOAuthResponse kakaoOAuthResponse) {
            return new UserCommand.Create(kakaoOAuthResponse.getEmail(),
                kakaoOAuthResponse.getNickname(), kakaoOAuthResponse.getProfileImageUrl(),
                UserType.KAKAO, Role.USER);
        }

        public User toEntity() {
            return new User(this.username, null, this.nickname, this.profileImageUrl, this.userType,
                this.role);
        }
    }

}
