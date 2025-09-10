package my.inplace.security.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import my.inplace.security.application.dto.CustomOAuth2User;
import my.inplace.security.application.dto.KakaoOAuthResponse;
import java.util.Optional;
import my.inplace.security.token.OauthTokenService;
import my.inplace.security.token.dto.TokenCommand;
import my.inplace.security.user.dto.UserSecurityCommand;
import my.inplace.security.user.dto.UserSecurityResult;
import my.inplace.security.user.UserSecurityService;

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
                TokenCommand.UpsertOauthToken.of(
                    userInfo.id(),
                    oAuth2AccessToken.getTokenValue(),
                    oAuth2AccessToken.getExpiresAt()
                ));
    }

    private void insertOauthToken(OAuth2AccessToken oAuth2AccessToken, UserSecurityResult.Info newUser) {
        oauthTokenService.insertOauthToken(
                TokenCommand.UpsertOauthToken.of(
                    newUser.id(),
                    oAuth2AccessToken.getTokenValue(),
                    oAuth2AccessToken.getExpiresAt()
                )
        );
    }
}
