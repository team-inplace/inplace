package my.inplace.security.token;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.security.token.dto.TokenResult;
import my.inplace.security.token.dto.TokenResult.ReIssued;
import my.inplace.security.user.dto.UserSecurityResult;
import my.inplace.security.user.UserSecurityService;
import my.inplace.security.util.JwtUtil;

@Component
@RequiredArgsConstructor
public class RefreshTokenFacade {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserSecurityService userSecurityService;

    @Transactional
    public ReIssued getReIssuedRefreshTokenCookie(String username, String refreshToken) throws InplaceException {
        refreshTokenService.checkInvalidToken(refreshToken);

        UserSecurityResult.Info userInfo = userSecurityService.findUserByUsername(username)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.NOT_FOUND));
        String reIssuedRefreshToken = jwtUtil
            .createRefreshToken(userInfo.username(), userInfo.id(), userInfo.role().getRoles());
        String reIssuedAccessToken = jwtUtil
            .createAccessToken(userInfo.username(), userInfo.id(), userInfo.role().getRoles());
        refreshTokenService.saveRefreshToken(username, reIssuedRefreshToken);

        return TokenResult.ReIssued.of(reIssuedAccessToken, reIssuedRefreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        String username = jwtUtil.getUsername(refreshToken);
        refreshTokenService.deleteRefreshToken(username);
    }
}
