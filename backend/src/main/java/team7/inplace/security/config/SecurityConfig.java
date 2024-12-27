package team7.inplace.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import team7.inplace.security.application.CustomOAuth2UserService;
import team7.inplace.security.entryPoint.LoginAuthenticationEntryPoint;
import team7.inplace.security.filter.AuthorizationFilter;
import team7.inplace.security.filter.ExceptionHandlingFilter;
import team7.inplace.security.handler.CustomAccessDeniedHandler;
import team7.inplace.security.handler.CustomFailureHandler;
import team7.inplace.security.handler.CustomSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOauth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final ExceptionHandlingFilter exceptionHandlingFilter;
    private final AuthorizationFilter authorizationFilter;
    private final CustomFailureHandler customFailureHandler;
    private final CorsFilter corsFilter;
    private final LoginAuthenticationEntryPoint loginAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {

        //http 설정
        http.csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)

            //authentication Service, Handler 설정
            .oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndPointConfig) -> userInfoEndPointConfig
                    .userService(customOauth2UserService)).successHandler(customSuccessHandler)
                .failureHandler(customFailureHandler))

            //authentication Filter 설정
            .addFilterBefore(authorizationFilter,
                UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(exceptionHandlingFilter, AuthorizationFilter.class)

            .exceptionHandling((auth) -> auth
                .authenticationEntryPoint(loginAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler))
            //authentication 경로 설정
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/error-logs/**", "/cicd", "crawling/**").hasRole("ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/users/**").authenticated()
                .requestMatchers("/influencers/likes").authenticated()
                .requestMatchers("/influencers/multiple/likes").authenticated()
                .requestMatchers(HttpMethod.POST, "/influencers/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/influencers/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/influencers/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/influencers/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/places/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/places/**").permitAll()
                .requestMatchers("/place-message/**").authenticated()
                .requestMatchers("/reviews/**").authenticated()
                .requestMatchers("/refresh-token").authenticated()
                .requestMatchers("/videos", "videos/my").authenticated()
                .requestMatchers("/videos/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/videos/{videoId}").authenticated()
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
