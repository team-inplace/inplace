package token.dto;

import java.time.Instant;

public class TokenCommand {

    public record UpsertOauthToken(
        Long userId,
        String oauthToken,
        Instant expiresAt
    ) {
        public static UpsertOauthToken of(Long userId, String oauthToken, Instant expiresAt) {
            return new UpsertOauthToken(userId, oauthToken, expiresAt);
        }
    }
}
