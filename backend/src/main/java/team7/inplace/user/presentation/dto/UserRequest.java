package team7.inplace.user.presentation.dto;

public class UserRequest {

    public record UpdateNickname(
        String nickname
    ) {

    }

    public record UpdateMainBadge(
        Long id
    ) {

    }
}
