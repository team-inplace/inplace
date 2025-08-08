package token;

import exception.InplaceException;
import exception.code.AuthorizationErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import security.RefreshToken;
import security.RefreshTokenRedisRepository;
import util.JwtUtil;

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
