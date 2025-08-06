package admin;

import user.AdminUser;
import user.Role;

public record AdminUserResult(
    Long id,
    String username,
    String password,
    Role role
) {

    public static AdminUserResult from(AdminUser adminUser) {
        return new AdminUserResult(
            adminUser.getId(),
            adminUser.getUsername(),
            adminUser.getPassword(),
            adminUser.getRole()
        );
    }
}
