package user.dto;

import user.Role;
import user.User;

public class UserSecurityResult {

    public record Info(
        Long id,
        String username,
        Role role
    ) {
        public static Info from(User user) {
            return new Info(
                user.getId(),
                user.getUsername(),
                user.getRole()
            );
        }
    }
}
