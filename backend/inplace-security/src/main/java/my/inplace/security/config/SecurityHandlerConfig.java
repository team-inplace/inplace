package my.inplace.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.inplace.security.handler.FormLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import my.inplace.security.handler.CustomAccessDeniedHandler;
import my.inplace.security.handler.CustomFailureHandler;
import my.inplace.security.handler.OAuth2SuccessHandler;
import my.inplace.security.token.RefreshTokenService;
import my.inplace.security.util.JwtUtil;

@Configuration
public class SecurityHandlerConfig {

    @Bean
    public OAuth2SuccessHandler customSuccessHandler(
        JwtUtil jwtUtil,
        RefreshTokenService refreshTokenService
    ) {
        return new OAuth2SuccessHandler(jwtUtil, refreshTokenService);
    }
    
    @Bean
    public FormLoginSuccessHandler formLoginSuccessHandler(
        JwtUtil jwtUtil
    ) {
        return new FormLoginSuccessHandler(jwtUtil);
    }

    @Bean
    public CustomFailureHandler customFailureHandler(ObjectMapper objectMapper) {
        return new CustomFailureHandler(objectMapper);
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
