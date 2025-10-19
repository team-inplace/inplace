package my.inplace.security.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import my.inplace.security.application.dto.CustomOAuth2User;
import my.inplace.security.application.dto.KakaoOAuthResponse;
import my.inplace.security.user.dto.UserSecurityCommand;
import my.inplace.security.user.dto.UserSecurityResult;
import my.inplace.security.user.UserSecurityService;

@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService defaultOAuth2UserService;
    private final UserSecurityService userSecurityService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest)
            throws OAuth2AuthenticationException {
        
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(oAuth2UserRequest);
        KakaoOAuthResponse kakaoOAuthResponse = new KakaoOAuthResponse(oAuth2User.getAttributes());
        
        return userSecurityService.findUserByUsername(kakaoOAuthResponse.getEmail())
            .map(userInfo -> handleExistingUser(userInfo, kakaoOAuthResponse))
            .orElseGet(() -> handleNewUser(kakaoOAuthResponse));
    }
    
    private CustomOAuth2User handleExistingUser(UserSecurityResult.Info user, KakaoOAuthResponse kakaoOAuthResponse) {
        userSecurityService.updateProfileImageUrl(user.id(), kakaoOAuthResponse.getProfileImageUrl());
        return CustomOAuth2User.makeExistUser(user);
    }
    
    private CustomOAuth2User handleNewUser(KakaoOAuthResponse kakaoOAuthResponse) {
        UserSecurityResult.Info newUser = userSecurityService.registerUser(
            UserSecurityCommand.Create.of(kakaoOAuthResponse)
        );
        return CustomOAuth2User.makeNewUser(newUser);
    }
}
