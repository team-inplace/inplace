package team7.inplace.token.presentation;

import static team7.inplace.security.filter.TokenType.ACCESS_TOKEN;
import static team7.inplace.security.filter.TokenType.REFRESH_TOKEN;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.security.util.CookieUtil;
import team7.inplace.security.util.JwtUtil;
import team7.inplace.token.application.RefreshTokenFacade;
import team7.inplace.token.application.dto.TokenCommand.ReIssued;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController implements RefreshTokenControllerApiSpec {

    private final JwtUtil jwtUtil;
    private final RefreshTokenFacade refreshTokenFacade;

    @GetMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(@CookieValue(value = "refresh_token") Cookie cookie,
        HttpServletResponse response) {

        String refreshToken = cookie.getValue();
        ReIssued reIssuedToken = refreshTokenFacade.getReIssuedRefreshTokenCookie(
            jwtUtil.getUsername(refreshToken), refreshToken);
        addTokenToCookie(response, reIssuedToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addTokenToCookie(HttpServletResponse response, ReIssued reIssuedToken) {
        ResponseCookie accessTokenCookie = CookieUtil.createCookie(ACCESS_TOKEN, reIssuedToken.accessToken());
        ResponseCookie refreshTokenCookie = CookieUtil.createCookie(REFRESH_TOKEN, reIssuedToken.refreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    @DeleteMapping("/refresh-token")
    public ResponseEntity<Void> deleteRefreshToken(@CookieValue(value = "refresh_token") Cookie cookie,
        HttpServletResponse response
    ) {
        String refreshToken = cookie.getValue();
        refreshTokenFacade.deleteRefreshToken(refreshToken);

        ResponseCookie accessTokenCookie = CookieUtil.createCookie(ACCESS_TOKEN, "");
        ResponseCookie refreshTokenCookie = CookieUtil.createCookie(REFRESH_TOKEN, "");
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
