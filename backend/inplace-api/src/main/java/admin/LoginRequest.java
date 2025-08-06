package admin;

import user.dto.AdminUserCommand;

public record LoginRequest(
    String username,
    String password
) {

    public static AdminUserCommand toRegisterCommand(LoginRequest loginRequest) {
        return new AdminUserCommand(loginRequest.username, loginRequest.password);
    }
}
