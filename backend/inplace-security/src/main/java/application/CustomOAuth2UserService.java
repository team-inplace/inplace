package application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import team7.inplace.token.application.OauthTokenService;
import team7.inplace.token.application.command.OauthTokenCommand;
import application.dto.CustomOAuth2User;
import application.dto.KakaoOAuthResponse;
import java.util.Optional;
import user.UserSecurityCommand;
import user.UserSecurityResult;
import user.UserSecurityService;

@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService defaultOAuth2UserService;
    private final UserSecurityService userSecurityService;
    private final OauthTokenService oauthTokenService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest)
            throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(oAuth2UserRequest);
        OAuth2AccessToken oAuth2AccessToken = oAuth2UserRequest.getAccessToken();
        KakaoOAuthResponse kakaoOAuthResponse = new KakaoOAuthResponse(oAuth2User.getAttributes());
        Optional<UserSecurityResult.Info> userInfo = userSecurityService.findUserByUsername(
                kakaoOAuthResponse.getEmail());
        if (userInfo.isPresent()) {
            updateOauthToken(oAuth2AccessToken, userInfo.get());
            userSecurityService.updateProfileImageUrl(userInfo.get().id(), kakaoOAuthResponse.getProfileImageUrl());
            return CustomOAuth2User.makeExistUser(userInfo.get());
        }
        UserSecurityResult.Info newUser = userSecurityService.registerUser(
                UserSecurityCommand.Create.of(kakaoOAuthResponse));
        insertOauthToken(oAuth2AccessToken, newUser);
        return CustomOAuth2User.makeNewUser(newUser);
    }

    private void updateOauthToken(OAuth2AccessToken oAuth2AccessToken, UserSecurityResult.Info userInfo) {
        oauthTokenService.updateOauthToken(
                OauthTokenCommand.of(
                        oAuth2AccessToken.getTokenValue(),
                        oAuth2AccessToken.getExpiresAt(),
                        userInfo.id()
                ));
    }

    private void insertOauthToken(OAuth2AccessToken oAuth2AccessToken, UserSecurityResult.Info newUser) {
        oauthTokenService.insertOauthToken(
                OauthTokenCommand.of(
                        oAuth2AccessToken.getTokenValue(),
                        oAuth2AccessToken.getExpiresAt(),
                        newUser.id()
                )
        );
    }
}
