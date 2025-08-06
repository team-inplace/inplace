package application.dto;

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

    public static CustomUserDetails makeUser(AdminUserInfo adminUserInfo) {
        return new CustomUserDetails(adminUserInfo.id(), adminUserInfo.username(), adminUserInfo.password(),
            adminUserInfo.role().getRoles());
    }
}
