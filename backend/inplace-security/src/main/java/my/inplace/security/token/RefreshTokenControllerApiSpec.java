package my.inplace.security.token;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import my.inplace.security.token.dto.TokenRequest;
import my.inplace.security.token.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

public interface RefreshTokenControllerApiSpec {

    @Operation(
        summary = "토큰 리프레시",
        description = "Cookie에 refreshToken을 보내면 accessToken과 refreshToken을 새로 발급함."
    )
    ResponseEntity<Void> refreshToken(
        @CookieValue(value = "refresh_token") Cookie cookie,
        HttpServletResponse response
    );
    
    @Operation(
        summary = "토큰 리프레시 (모바일)",
        description = "Body에 ReIssued된 토큰 정보를 반환함."
    )
    ResponseEntity<TokenResponse> mobileRefreshToken(
        @RequestBody TokenRequest tokenRequest
    );
}
