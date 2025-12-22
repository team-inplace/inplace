package my.inplace.security.config;

import my.inplace.domain.security.OAuthSecurityClient;
import my.inplace.infra.security.KakaoOAuthClient;
import my.inplace.security.application.AdminUserService;
import my.inplace.security.application.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import my.inplace.security.application.CustomOAuth2UserService;
import my.inplace.security.user.UserSecurityService;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityServiceConfig {

    @Bean
    public DefaultOAuth2UserService defaultOAuth2UserService() {
        return new DefaultOAuth2UserService();
    }
    
    @Bean
    public OAuthSecurityClient oauthSecurityClient(
        RestTemplate restTemplate
    ) {
        return new KakaoOAuthClient(restTemplate);
    }
    
    @Bean
    public CustomOAuth2UserService customOAuth2UserService(
        DefaultOAuth2UserService defaultOAuth2UserService,
        UserSecurityService userSecurityService,
        OAuthSecurityClient oauthSecurityClient
    ) {
        return new CustomOAuth2UserService(defaultOAuth2UserService, userSecurityService, oauthSecurityClient);
    }
    
    @Bean
    public CustomUserDetailsService customUserDetailsService(AdminUserService adminUserService) {
        return new CustomUserDetailsService(adminUserService);
    }
}
