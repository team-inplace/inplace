package team7.inplace.security.filter;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.security.application.dto.CustomOAuth2User;
import team7.inplace.security.util.JwtUtil;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Value("${spring.application.domain}")
    private String domain;

    public AuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        if (hasNoTokenCookie(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = getAccessToken(request);
        if (StringUtils.isNotEmpty(accessToken) && jwtUtil.isNotExpired(accessToken)) {
            addUserToAuthentication(accessToken);
        }

        String refreshToken = getRefreshToken(request);
        if (StringUtils.isNotEmpty(refreshToken) && jwtUtil.isNotExpired(refreshToken)) {
            addUserToAuthentication(refreshToken);
        }

        filterChain.doFilter(request, response);
    }

    private boolean hasNoTokenCookie(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
            .flatMap(cookies -> Arrays.stream(cookies)
                .filter(
                    cookie -> cookie.getName().equals(TokenType.ACCESS_TOKEN.getValue())
                        || cookie.getName().equals(TokenType.REFRESH_TOKEN.getValue())
                )
                .findAny())
            .isEmpty();
    }

    private String getAccessToken(HttpServletRequest request) throws InplaceException {
        Cookie accessTokenCookie = Arrays.stream(request.getCookies())
            .filter(
                cookie -> cookie.getName().equals(TokenType.ACCESS_TOKEN.getValue()))
            .findAny()
            .orElse(null);
        if (Objects.isNull(accessTokenCookie)) {
            return null;
        }
        return accessTokenCookie.getValue();
    }

    private String getRefreshToken(HttpServletRequest request) throws InplaceException {
        Cookie refreshTokenCookie = Arrays.stream(request.getCookies())
            .filter(
                cookie -> cookie.getName().equals(TokenType.REFRESH_TOKEN.getValue()))
            .findAny()
            .orElse(null);
        if (Objects.isNull(refreshTokenCookie)) {
            return null;
        }
        return refreshTokenCookie.getValue();
    }

    private void addUserToAuthentication(String token) throws InplaceException {
        String username = jwtUtil.getUsername(token);
        Long id = jwtUtil.getId(token);
        String roles = jwtUtil.getRoles(token);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(username, id, roles);
        Authentication authToken = new OAuth2AuthenticationToken(customOAuth2User,
            customOAuth2User.getAuthorities(), customOAuth2User.getRegistrationId());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

}
