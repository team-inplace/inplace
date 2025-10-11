package my.inplace.security.user.dto;

import my.inplace.security.application.dto.KakaoOAuthResponse;
import my.inplace.domain.user.Role;
import my.inplace.domain.user.User;
import my.inplace.domain.user.UserType;

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
