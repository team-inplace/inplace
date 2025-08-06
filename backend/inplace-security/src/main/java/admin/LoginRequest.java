package admin;

public record LoginRequest(
    String username,
    String password
) {

    public static AdminUserCommand toAdminUserCommand(LoginRequest loginRequest) {
        return new AdminUserCommand(loginRequest.username, loginRequest.password);
    }
}
