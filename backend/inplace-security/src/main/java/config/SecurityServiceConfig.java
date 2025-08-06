package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import team7.inplace.admin.user.application.AdminUserService;
import application.CustomOAuth2UserService;
import application.CustomUserDetailsService;
import team7.inplace.token.application.OauthTokenService;
import team7.inplace.user.application.UserService;

@Configuration
public class SecurityServiceConfig {

    @Bean
    public DefaultOAuth2UserService defaultOAuth2UserService() {
        return new DefaultOAuth2UserService();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService(
        DefaultOAuth2UserService defaultOAuth2UserService,
        UserService userService,
        OauthTokenService oauthTokenService
    ) {
        return new CustomOAuth2UserService(defaultOAuth2UserService, userService,
            oauthTokenService);
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService(AdminUserService adminUserService) {
        return new CustomUserDetailsService(adminUserService);
    }
}
