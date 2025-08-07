package token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.OauthToken;
import security.jpa.OauthTokenJpaRepository;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.UserErrorCode;
import team7.inplace.security.util.TokenEncryptionUtil;
import team7.inplace.token.application.command.OauthTokenCommand;
import team7.inplace.token.client.KakaoOauthClient;
import team7.inplace.user.domain.User;
import user.jpa.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class OauthTokenService {

    private final UserJpaRepository userJpaRepository;
    private final KakaoOauthClient kakaoOauthClient;
    private final TokenEncryptionUtil tokenEncryptionUtil;
    private final OauthTokenJpaRepository oauthTokenJpaRepository;

    @Transactional(readOnly = true)
    public String findOAuthTokenByUserId(Long userId) {
        var token = oauthTokenJpaRepository.findByUserId(userId)
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

        oauthTokenJpaRepository.save(oauthToken);
    }

    @Transactional
    public void updateOauthToken(OauthTokenCommand oauthTokenCommand) {
        OauthToken oauthToken = oauthTokenJpaRepository.findByUserId(oauthTokenCommand.userId())
            .orElseThrow(() -> InplaceException.of(UserErrorCode.OAUTH_TOKEN_NOT_FOUND));

        oauthToken.updateInfo(
            tokenEncryptionUtil.encrypt(oauthTokenCommand.oauthToken()),
            oauthTokenCommand.expiresAt()
        );
    }

    public void unlinkOauthToken(Long userId) {
        OauthToken oauthToken = oauthTokenJpaRepository.findByUserId(userId)
            .orElseThrow(() -> InplaceException.of(UserErrorCode.OAUTH_TOKEN_NOT_FOUND));

        var token = tokenEncryptionUtil.decrypt(oauthToken.getOauthToken());
        kakaoOauthClient.unLink(token);
        oauthTokenJpaRepository.delete(oauthToken);
    }
}
