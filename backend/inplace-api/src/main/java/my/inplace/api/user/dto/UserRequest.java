package my.inplace.api.user.dto;

public class UserRequest {

    public record UpdateNickname(
        String nickname
    ) {

    }

    public record UpdateMainBadge(
        Long id
    ) {

    }
    
    public record UpdatePushResent(
        Boolean isResented
    ) {
    
    }
}
