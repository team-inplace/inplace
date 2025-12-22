package my.inplace.security.token.dto;

public record TokenResponse(
    String accessToken,
    String refreshToken
) {
    public static TokenResponse from(TokenResult.ReIssued tokenResult) {
        return new TokenResponse(tokenResult.accessToken(), tokenResult.refreshToken());
    }
}
