package team7.inplace.token.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.UserErrorCode;
import team7.inplace.security.util.TokenEncryptionUtil;
import team7.inplace.token.application.command.OauthTokenCommand;
import team7.inplace.token.client.KakaoOauthClient;
import team7.inplace.token.domain.OauthToken;
import team7.inplace.token.persistence.OauthTokenRepository;
import team7.inplace.user.domain.User;
import team7.inplace.user.persistence.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class OauthTokenService {

    private final UserJpaRepository userJpaRepository;
    private final KakaoOauthClient kakaoOauthClient;
    private final TokenEncryptionUtil tokenEncryptionUtil;
    private final OauthTokenRepository oauthTokenRepository;

    @Transactional(readOnly = true)
    public String findOAuthTokenByUserId(Long userId) {
        var token = oauthTokenRepository.findByUserId(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.OAUTH_TOKEN_NOT_FOUND));

        return tokenEncryptionUtil.decrypt(token.getOauthToken());
    }

    @Transactional
    public void insertOauthToken(OauthTokenCommand oauthTokenCommand) {
        User userProxy = userJpaRepository.getReferenceById(oauthTokenCommand.userId());
        OauthToken oauthToken = OauthToken.of(
            tokenEncryptionUtil.encrypt(oauthTokenCommand.oauthToken()),
            oauthTokenCommand.expiresAt(),
            userProxy
        );

        oauthTokenRepository.save(oauthToken);
    }

    @Transactional
    public void updateOauthToken(OauthTokenCommand oauthTokenCommand) {
        OauthToken oauthToken = oauthTokenRepository.findByUserId(oauthTokenCommand.userId())
            .orElseThrow(() -> InplaceException.of(UserErrorCode.OAUTH_TOKEN_NOT_FOUND));

        oauthToken.updateInfo(
            tokenEncryptionUtil.encrypt(oauthTokenCommand.oauthToken()),
            oauthTokenCommand.expiresAt()
        );
    }

    public void unlinkOauthToken(Long userId) {
        OauthToken oauthToken = oauthTokenRepository.findByUserId(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.OAUTH_TOKEN_NOT_FOUND));

        var token = tokenEncryptionUtil.decrypt(oauthToken.getOauthToken());
        kakaoOauthClient.unlink(token);
        oauthTokenRepository.delete(oauthToken);
    }
}
