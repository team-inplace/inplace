package my.inplace.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import my.inplace.security.application.dto.CustomOAuth2User;
import my.inplace.security.application.dto.CustomUserDetails;
import my.inplace.security.filter.TokenType;
import my.inplace.security.token.RefreshTokenService;
import my.inplace.security.util.CookieUtil;
import my.inplace.security.util.JwtUtil;

@Slf4j
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private static final String IS_FIRST_USER = "is_first_user";
    private static final String FIRST_USER_SUFFIX = "/choice";
    private static final String NOT_FIRST_USER_SUFFIX = "/auth";
    
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Value("${spring.redirect.front-end-url}")
    private String frontEndUrl;

    @Value("${spring.application.domain}")
    private String domain;

    public CustomSuccessHandler(
        JwtUtil jwtUtil,
        RefreshTokenService refreshTokenService
    ) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomOAuth2User customOAuth2User) {
            setTokenToCookie(response, customOAuth2User);
            setFirstUserToCookie(response, customOAuth2User.isFirstUser());
            
            String redirectUrl = frontEndUrl;
            redirectUrl += customOAuth2User.isFirstUser()
                ? FIRST_USER_SUFFIX
                : NOT_FIRST_USER_SUFFIX;
            response.sendRedirect(redirectUrl);
        }
        
        CustomUserDetails customUserDetails = (CustomUserDetails) principal;
        String accessToken = jwtUtil.createAccessToken(customUserDetails.username(),
            customUserDetails.id(), customUserDetails.roles());
        setCookie(response, TokenType.ACCESS_TOKEN.getValue(), accessToken);
        response.sendRedirect("/admin/main");
    }
    
    private void setTokenToCookie(HttpServletResponse response, CustomOAuth2User user) {
        String accessToken = jwtUtil.createAccessToken(user.username(), user.id(), user.roles());
        setCookie(response, TokenType.ACCESS_TOKEN.getValue(), accessToken);
        
        String refreshToken = jwtUtil.createRefreshToken(user.username(), user.id(), user.roles());
        setCookie(response, TokenType.REFRESH_TOKEN.getValue(), refreshToken);
        refreshTokenService.saveRefreshToken(user.username(), refreshToken);
    }

    private void setFirstUserToCookie(HttpServletResponse response, Boolean isFirstUser) {
        setCookie(response, IS_FIRST_USER, isFirstUser.toString());
    }

    private void setCookie(HttpServletResponse response, String key, String value) {
        ResponseCookie cookie = CookieUtil.createHttpOnlyCookie(key, value, domain);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
