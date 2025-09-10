package my.inplace.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import my.inplace.security.application.CustomOAuth2UserService;
import my.inplace.security.filter.AuthorizationFilter;
import my.inplace.security.filter.ExceptionHandlingFilter;
import my.inplace.security.handler.CustomAccessDeniedHandler;
import my.inplace.security.handler.CustomFailureHandler;
import my.inplace.security.handler.CustomSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomOAuth2UserService customOauth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final ExceptionHandlingFilter exceptionHandlingFilter;
    private final AuthorizationFilter authorizationFilter;
    private final CustomFailureHandler customFailureHandler;
    private final CorsFilter corsFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {

        //http 설정
        http.csrf(AbstractHttpConfigurer::disable)
            .formLogin((form) -> form
                .loginPage("/admin/login")
                .successHandler(customSuccessHandler))
            .httpBasic(AbstractHttpConfigurer::disable)
            //authentication Service, Handler 설정
            .oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndPointConfig) -> userInfoEndPointConfig
                    .userService(customOauth2UserService))
                .successHandler(customSuccessHandler)
                .failureHandler(customFailureHandler))

            //authentication Filter 설정
            .addFilterBefore(authorizationFilter,
                UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(exceptionHandlingFilter, AuthorizationFilter.class)

            .exceptionHandling((auth) -> auth
                .accessDeniedHandler(customAccessDeniedHandler))
            //authentication 경로 설정
            .authorizeHttpRequests((auth) -> auth
                    .requestMatchers("/admin/login", "/admin/register").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().permitAll()
            )
            //cors 설정
            .addFilter(corsFilter)
            //session 설정
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
