package my.inplace.security.token;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.AuthorizationErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import my.inplace.domain.security.RefreshToken;
import my.inplace.infra.security.RefreshTokenRedisRepository;
import my.inplace.security.util.JwtUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRedisRepository refreshTokenRepository;

    public void checkInvalidToken(String refreshToken) throws InplaceException {
        String username = jwtUtil.getUsername(refreshToken);
        refreshTokenRepository.get(username)
            .orElseThrow(() -> InplaceException.of(AuthorizationErrorCode.INVALID_TOKEN))
            .checkValidToken(refreshToken);
    }

    public void saveRefreshToken(String username, String token) {
        RefreshToken refreshToken = new RefreshToken(username, token);
        refreshTokenRepository.save(username, refreshToken, jwtUtil.getRefreshTokenExpiredTime());
    }

    public void deleteRefreshToken(String username) {
        refreshTokenRepository.delete(username);
    }
}
