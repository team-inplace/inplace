package my.inplace.security.token;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.UserErrorCode;
import lombok.RequiredArgsConstructor;
import my.inplace.infra.token.KakaoOauthClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.domain.security.OauthToken;
import my.inplace.infra.security.jpa.OauthTokenJpaRepository;
import my.inplace.security.token.dto.TokenCommand;
import my.inplace.domain.user.User;
import my.inplace.infra.user.jpa.UserJpaRepository;
import my.inplace.security.util.TokenEncryptionUtil;

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
    public void insertOauthToken(TokenCommand.UpsertOauthToken upsertOauthToken) {
        User userProxy = userJpaRepository.getReferenceById(upsertOauthToken.userId());
        OauthToken oauthToken = OauthToken.of(
            tokenEncryptionUtil.encrypt(upsertOauthToken.oauthToken()),
            upsertOauthToken.expiresAt(),
            userProxy
        );

        oauthTokenJpaRepository.save(oauthToken);
    }

    @Transactional
    public void updateOauthToken(TokenCommand.UpsertOauthToken upsertOauthToken) {
        OauthToken oauthToken = oauthTokenJpaRepository.findByUserId(upsertOauthToken.userId())
            .orElseThrow(() -> InplaceException.of(UserErrorCode.OAUTH_TOKEN_NOT_FOUND));

        oauthToken.updateInfo(
            tokenEncryptionUtil.encrypt(upsertOauthToken.oauthToken()),
            upsertOauthToken.expiresAt()
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
