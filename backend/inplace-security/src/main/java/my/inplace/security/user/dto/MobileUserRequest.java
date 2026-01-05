package my.inplace.security.user.dto;

public record MobileUserRequest(
    String nickname,
    String username,
    String profileImageUrl
) {
}
