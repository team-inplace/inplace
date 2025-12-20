package my.inplace.security.user;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;

@Component
public class OAuth2RequestFactory {
    
    private final ClientRegistrationRepository clientRegistrationRepository;
    
    public OAuth2RequestFactory(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }
    
    public OAuth2UserRequest createKakaoUserRequest(String accessTokenValue) {
        ClientRegistration kakaoRegistration = clientRegistrationRepository.findByRegistrationId("kakao");
        
        if (kakaoRegistration == null) {
            throw new IllegalStateException("Kakao ClientRegistration not found in configuration");
        }
        
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            accessTokenValue,
            Instant.now(),
            Instant.now().plusSeconds(3600)
        );
        
        return new OAuth2UserRequest(kakaoRegistration, accessToken, Collections.emptyMap());
    }
}
