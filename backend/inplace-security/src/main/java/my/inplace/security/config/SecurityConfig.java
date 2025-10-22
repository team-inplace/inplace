package my.inplace.security.config;

import lombok.RequiredArgsConstructor;
import my.inplace.security.handler.FormLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
import my.inplace.security.handler.OAuth2SuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomOAuth2UserService customOauth2UserService;
    private final OAuth2SuccessHandler OAuth2SuccessHandler;
    private final FormLoginSuccessHandler formLoginSuccessHandler;
    private final AuthenticationManager authenticationManager;
    private final CustomFailureHandler customFailureHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final ExceptionHandlingFilter exceptionHandlingFilter;
    private final AuthorizationFilter authorizationFilter;
    private final CorsFilter corsFilter;
    
    /**
     *   Spring Security Filter Chain Definition
     * - csrf 설정 해제
     * - http basic 설정 해제
     * - 어드인 페이지 접근 권한 설정
     * - 어드민 로그인 핸들러 정의
     * - 어드민 로그인 처리를 위한 매니저 설정
     * - Oauth2 로그인 핸들러 정의
     * - 인가 실패 핸들러 정의
     * - 인증 필터 추가
     * - 예외 처리용 필터 추가
     * - CORS 설정 필터 추가
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {
        
        http.csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/admin/register", "/admin/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().permitAll())
            
            .formLogin((form) -> form
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .successHandler(formLoginSuccessHandler))
            
            .authenticationManager(authenticationManager)
            
            .oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndPointConfig) -> userInfoEndPointConfig
                    .userService(customOauth2UserService))
                .successHandler(OAuth2SuccessHandler)
                .failureHandler(customFailureHandler))
            
            .exceptionHandling((auth) -> auth
                .accessDeniedHandler(customAccessDeniedHandler))
            
            .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(exceptionHandlingFilter, AuthorizationFilter.class)
            .addFilter(corsFilter);
            
        return http.build();
    }
}
