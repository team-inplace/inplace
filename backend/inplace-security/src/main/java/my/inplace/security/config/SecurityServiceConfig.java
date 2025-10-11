package my.inplace.security.config;

import my.inplace.security.admin.AdminUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import my.inplace.security.application.CustomOAuth2UserService;
import my.inplace.security.application.CustomUserDetailsService;
import my.inplace.security.token.OauthTokenService;
import my.inplace.security.user.UserSecurityService;

@Configuration
public class SecurityServiceConfig {

    @Bean
    public DefaultOAuth2UserService defaultOAuth2UserService() {
        return new DefaultOAuth2UserService();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService(
        DefaultOAuth2UserService defaultOAuth2UserService,
        UserSecurityService userSecurityService,
        OauthTokenService oauthTokenService
    ) {
        return new CustomOAuth2UserService(defaultOAuth2UserService, userSecurityService,
            oauthTokenService);
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService(AdminUserService adminUserService) {
        return new CustomUserDetailsService(adminUserService);
    }
}
