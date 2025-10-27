package my.inplace.security.user;

import lombok.RequiredArgsConstructor;
import my.inplace.security.application.CustomOAuth2UserService;
import my.inplace.security.application.dto.CustomOAuth2User;
import my.inplace.security.user.dto.TokenResult;
import my.inplace.security.util.JwtUtil;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MobileUserFacade {
    
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2RequestFactory oauth2RequestFactory;
    private final JwtUtil jwtUtil;
    
    public TokenResult mobileLogin(String oauth2AccessToken) {
        OAuth2UserRequest kakaoUserRequest = oauth2RequestFactory.createKakaoUserRequest(oauth2AccessToken);
        CustomOAuth2User user = (CustomOAuth2User) customOAuth2UserService.loadUser(kakaoUserRequest);
        
        String accessToken = jwtUtil.createAccessToken(user.username(), user.id(), user.roles());
        String refreshToken = jwtUtil.createRefreshToken(user.username(), user.id(), user.roles());
        
        return new TokenResult(accessToken, refreshToken);
    }
}
