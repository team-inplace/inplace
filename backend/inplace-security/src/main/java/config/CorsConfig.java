package config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Value("${cors.origins}")
    private String[] allowedOrigins;

    @Bean
    public CorsFilter corsFilterLocal() {
        return createCorsFilter(allowedOrigins);
    }

    private CorsFilter createCorsFilter(String... allowedOriginPatterns) {
        var source = new UrlBasedCorsConfigurationSource();
        var config = createCorsConfiguration(allowedOriginPatterns);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    private CorsConfiguration createCorsConfiguration(String... allowedOriginPatterns) {
        var config = new CorsConfiguration();
        config.setAllowCredentials(true);
        Arrays.stream(allowedOriginPatterns).forEach(config::addAllowedOriginPattern);

        // 공통 헤더 설정
        Arrays.asList(
            "Origin", "Accept", "X-Requested-With", "Content-Type",
            "Access-Control-Request-Method", "Access-Control-Request-Headers",
            "Authorization", "Baggage", "sentry-trace"
        ).forEach(config::addAllowedHeader);

        // 공통 메소드 설정
        Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ).forEach(config::addAllowedMethod);

        config.setMaxAge(3600L);
        config.setExposedHeaders(Arrays.asList("Location", "Set-Cookie"));

        return config;
    }
}
