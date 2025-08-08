package admin.dto;

import user.AdminUser;
import user.Role;

public class AdminResult {
    public record AuthInfo(
        Long id,
        String username,
        String password,
        Role role
    ) {
        public static AuthInfo from(AdminUser adminUser) {
            return new AuthInfo(
                adminUser.getId(),
                adminUser.getUsername(),
                adminUser.getPassword(),
                adminUser.getRole()
            );
        }
    }
}
