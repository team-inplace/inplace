package my.inplace.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import my.inplace.security.handler.CustomAccessDeniedHandler;
import my.inplace.security.handler.CustomFailureHandler;
import my.inplace.security.handler.CustomSuccessHandler;
import my.inplace.security.token.RefreshTokenService;
import my.inplace.security.util.JwtUtil;

@Configuration
public class SecurityHandlerConfig {

    @Bean
    public CustomSuccessHandler customSuccessHandler(
            JwtUtil jwtUtil,
            RefreshTokenService refreshTokenService
    ) {
        return new CustomSuccessHandler(jwtUtil, refreshTokenService);
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
