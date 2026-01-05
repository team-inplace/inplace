package my.inplace.security.user.dto;

import my.inplace.domain.user.Role;
import my.inplace.domain.user.User;
import my.inplace.domain.user.UserType;
import my.inplace.security.application.dto.KakaoOAuthResponse;

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
        
        public static UserSecurityCommand.Create from(String email,String nickname, String profileImageUrl) {
            return new UserSecurityCommand.Create(email, nickname, profileImageUrl, UserType.KAKAO, Role.USER);
        }

        public User toEntity() {
            return new User(this.username, null, this.nickname, this.profileImageUrl, this.userType,
                this.role);
        }
    }

}
