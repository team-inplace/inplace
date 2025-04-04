package team7.inplace.token.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.security.util.JwtUtil;
import team7.inplace.token.application.command.TokenCommand;
import team7.inplace.token.application.command.TokenCommand.ReIssued;
import team7.inplace.user.application.UserService;
import team7.inplace.user.application.dto.UserCommand;

@Component
@RequiredArgsConstructor
public class RefreshTokenFacade {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @Transactional
    public ReIssued getReIssuedRefreshTokenCookie(String username, String refreshToken) throws InplaceException {
        refreshTokenService.checkInvalidToken(refreshToken);

        UserCommand.Info userInfo = userService.getUserByUsername(username);
        String reIssuedRefreshToken = jwtUtil
                .createRefreshToken(userInfo.username(), userInfo.id(), userInfo.role().getRoles());
        String reIssuedAccessToken = jwtUtil
                .createAccessToken(userInfo.username(), userInfo.id(), userInfo.role().getRoles());
        refreshTokenService.saveRefreshToken(username, reIssuedRefreshToken);

        return TokenCommand.ReIssued.of(reIssuedAccessToken, reIssuedRefreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        String username = jwtUtil.getUsername(refreshToken);
        refreshTokenService.deleteRefreshToken(username);
    }
}
