package team7.inplace.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import team7.inplace.security.application.dto.CustomOAuth2User;
import team7.inplace.security.util.JwtUtil;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        Cookie authorizationCookie = getAuthorizationCookie(request);

        if (isCookieValidated(authorizationCookie)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationCookie.getValue();
        String username = jwtUtil.getUsername(token);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(username, null, null);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

    private Cookie getAuthorizationCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals("Authorization"))
            .findFirst().orElse(null);
    }

    private boolean isCookieValidated(Cookie authorizationCookie) {
        return isCookieEmpty(authorizationCookie) || jwtUtil.isExpired(
            authorizationCookie.getValue());
    }

    private boolean isCookieEmpty(Cookie authorizationCookie) {
        return authorizationCookie == null || authorizationCookie.getValue() == null;
    }

}
