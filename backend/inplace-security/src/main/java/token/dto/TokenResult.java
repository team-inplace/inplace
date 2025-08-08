package token.dto;

public class TokenResult {

    public record ReIssued(
        String accessToken,
        String refreshToken
    ) {

        public static ReIssued of(String reIssuedAccessToken, String reIssuedRefreshToken) {
            return new ReIssued(reIssuedAccessToken, reIssuedRefreshToken);
        }
    }
}
