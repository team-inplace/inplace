package my.inplace.domain.security;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.AuthorizationErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    private String username;

    private String refreshToken;

    public void checkValidToken(String refreshToken) {
        if (this.refreshToken.equals(refreshToken)) {
            return;
        }
        throw InplaceException.of(AuthorizationErrorCode.INVALID_TOKEN);
    }
}
