package my.inplace.security.user.dto;

public class MobileUserResponse {
    public record TokenResponse(String accessToken, String refreshToken){
        public static TokenResponse from(TokenResult tokenResult) {
            return new TokenResponse(tokenResult.accessToken(), tokenResult.refreshToken());
        }
    }
}
