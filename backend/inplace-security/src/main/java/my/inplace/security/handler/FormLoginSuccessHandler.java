package my.inplace.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import my.inplace.security.application.dto.CustomUserDetails;
import my.inplace.security.filter.TokenType;
import my.inplace.security.util.CookieUtil;
import my.inplace.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;


@Slf4j
public class FormLoginSuccessHandler implements AuthenticationSuccessHandler {
    
    private final JwtUtil jwtUtil;
    
    @Value("${spring.application.domain}")
    private String domain;
    
    public FormLoginSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        
        String accessToken = jwtUtil.createAccessToken(customUserDetails.username(),
            customUserDetails.id(), customUserDetails.roles());
        setCookie(response, TokenType.ACCESS_TOKEN.getValue(), accessToken);
        response.sendRedirect("/admin/main");
    }
    
    private void setCookie(HttpServletResponse response, String key, String value) {
        ResponseCookie cookie = CookieUtil.createHttpOnlyCookie(key, value, domain);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
