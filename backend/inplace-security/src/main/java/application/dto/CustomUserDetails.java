package application.dto;

import admin.AdminUserResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import team7.inplace.admin.user.application.dto.AdminUserInfo;

public record CustomUserDetails(
    Long id,
    String username,
    String password,
    String roles,
    Collection<GrantedAuthority> authorities
) implements UserDetails {

    public CustomUserDetails(Long id, String username, String password, String roles) {
        this(id, username, password, roles, createAuthorities(roles));
    }

    private static Collection<GrantedAuthority> createAuthorities(String roles) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        for (String role : roles.split(",")) {
            if (!StringUtils.hasText(role)) {
                continue;
            }
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public static CustomUserDetails makeUser(AdminUserResult adminUserResult) {
        return new CustomUserDetails(adminUserResult.id(), adminUserResult.username(), adminUserResult.password(),
            adminUserResult.role().getRoles());
    }
}
