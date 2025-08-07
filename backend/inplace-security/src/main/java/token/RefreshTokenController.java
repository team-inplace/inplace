package token;

import filter.TokenType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CookieUtil;
import util.JwtUtil;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class RefreshTokenController implements RefreshTokenControllerApiSpec {

    private final JwtUtil jwtUtil;
    private final RefreshTokenFacade refreshTokenFacade;

    @Value("${spring.application.domain}")
    private String domain;

    @GetMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(
        @CookieValue(value = "refresh_token") Cookie cookie,
        HttpServletResponse response
    ) {

        String refreshToken = cookie.getValue();
        TokenResult.ReIssued reIssuedToken = refreshTokenFacade.getReIssuedRefreshTokenCookie(
            jwtUtil.getUsername(refreshToken), refreshToken);
        addTokenToCookie(response, reIssuedToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addTokenToCookie(HttpServletResponse response, TokenResult.ReIssued reIssuedToken) {
        var accessTokenCookie = CookieUtil.createHttpOnlyCookie(TokenType.ACCESS_TOKEN.getValue(),
            reIssuedToken.accessToken(), domain);
        var refreshTokenCookie = CookieUtil.createHttpOnlyCookie(TokenType.REFRESH_TOKEN.getValue(),
            reIssuedToken.refreshToken(), domain);

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    @DeleteMapping("/refresh-token")
    public ResponseEntity<Void> deleteRefreshToken(
        @CookieValue(value = "refresh_token") Cookie cookie,
        HttpServletResponse response
    ) {
        String refreshToken = cookie.getValue();
        refreshTokenFacade.deleteRefreshToken(refreshToken);

        ResponseCookie accessTokenCookie = CookieUtil.createHttpOnlyCookie(
            TokenType.ACCESS_TOKEN.getValue(), "", domain);
        ResponseCookie refreshTokenCookie = CookieUtil.createHttpOnlyCookie(
            TokenType.REFRESH_TOKEN.getValue(), "", domain);
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
