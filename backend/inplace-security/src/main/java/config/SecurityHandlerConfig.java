package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import handler.CustomAccessDeniedHandler;
import handler.CustomFailureHandler;
import handler.CustomSuccessHandler;
import token.RefreshTokenService;
import util.JwtUtil;

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
