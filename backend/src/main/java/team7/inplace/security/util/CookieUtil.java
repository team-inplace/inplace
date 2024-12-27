package team7.inplace.security.util;

import org.springframework.http.ResponseCookie;
import team7.inplace.security.filter.TokenType;

public class CookieUtil {

    public static ResponseCookie createCookie(TokenType tokenType, String value) {
        return ResponseCookie.from(tokenType.getValue(), value)
            .sameSite("None")
            .secure(true)
            .path("/")
            .httpOnly(true)
            .domain("inplace.my")
            .maxAge(60 * 60)
            .build();
    }
}
