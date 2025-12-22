package my.inplace.security.token;

import my.inplace.security.filter.TokenType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import my.inplace.security.token.dto.TokenRequest;
import my.inplace.security.token.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import my.inplace.security.token.dto.TokenResult;
import my.inplace.security.util.CookieUtil;
import my.inplace.security.util.JwtUtil;

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
    
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> mobileRefreshToken(
        @RequestBody TokenRequest tokenRequest
    ) {
        TokenResult.ReIssued reIssuedToken = refreshTokenFacade.getReIssuedRefreshTokenCookie(
            jwtUtil.getUsername(tokenRequest.refreshToken()), tokenRequest.refreshToken()
        );
        TokenResponse response = TokenResponse.from(reIssuedToken);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
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
