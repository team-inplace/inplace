package user.dto;

import application.dto.KakaoOAuthResponse;
import user.Role;
import user.User;
import user.UserType;

public class UserSecurityCommand {

    public record Create(
        String username,
        String nickname,
        String profileImageUrl,
        UserType userType,
        Role role
    ) {

        public static UserSecurityCommand.Create of(KakaoOAuthResponse kakaoOAuthResponse) {
            return new UserSecurityCommand.Create(kakaoOAuthResponse.getEmail(),
                kakaoOAuthResponse.getNickname(), kakaoOAuthResponse.getProfileImageUrl(),
                UserType.KAKAO, Role.USER);
        }

        public User toEntity() {
            return new User(this.username, null, this.nickname, this.profileImageUrl, this.userType,
                this.role);
        }
    }

}
