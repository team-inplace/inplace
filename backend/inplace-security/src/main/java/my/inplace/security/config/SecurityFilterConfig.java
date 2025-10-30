package my.inplace.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import my.inplace.security.filter.ApiExecutionMetricsFilter;
import my.inplace.security.filter.AuthorizationFilter;
import my.inplace.security.filter.ExceptionHandlingFilter;
import my.inplace.security.util.JwtUtil;

@Configuration
public class SecurityFilterConfig {

    @Bean
    public AuthorizationFilter authorizationFilter(JwtUtil jwtUtil) {
        return new AuthorizationFilter(jwtUtil);
    }

    @Bean
    public ExceptionHandlingFilter exceptionHandlingFilter(ObjectMapper objectMapper) {
        return new ExceptionHandlingFilter(objectMapper);
    }

    @Bean
    public ApiExecutionMetricsFilter apiExecutionMetricsFilter(
        MeterRegistry meterRegistry, ObjectMapper objectMapper
    ) {
        return new ApiExecutionMetricsFilter(meterRegistry, objectMapper);
    }
}
