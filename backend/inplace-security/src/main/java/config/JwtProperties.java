package config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(JwtProperties.class)
@ConfigurationProperties(prefix = "spring.jwt")
public record JwtProperties(
        String secret,
        Long accessTokenExpiredTime,
        Long refreshTokenExpiredTime,
        Long adminAccessTokenExpiredTime
) {

}
