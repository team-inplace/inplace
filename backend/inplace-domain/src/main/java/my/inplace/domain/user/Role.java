package my.inplace.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_USER,ROLE_ADMIN"),
    FIRST_USER("ROLE_FIRST_USER");

    private final String roles;

    public static String addRole(Role orgRole, Role otherRole) {
        return orgRole.getRoles() + "," + otherRole.getRoles();
    }
}
