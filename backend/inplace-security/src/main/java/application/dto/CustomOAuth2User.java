package application.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.StringUtils;
import user.Role;
import user.dto.UserSecurityResult;

public record CustomOAuth2User(
    String username,
    Long id,
    String roles,
    Collection<GrantedAuthority> authorities
) implements OAuth2User {

    public CustomOAuth2User(String username, Long id, String roles) {
        this(username, id, roles, createAuthorities(roles));
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

    public static CustomOAuth2User makeExistUser(UserSecurityResult.Info user) {
        return new CustomOAuth2User(user.username(), user.id(), user.role().getRoles());
    }

    public static CustomOAuth2User makeNewUser(UserSecurityResult.Info user) {
        return new CustomOAuth2User(user.username(), user.id(),
            Role.addRole(Role.USER, Role.FIRST_USER));
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return username;
    }

    public String getRegistrationId() {
        return "kakao";
    }

    public boolean isFirstUser() {
        return this.authorities.stream()
            .anyMatch(authority -> authority.getAuthority().equals(Role.FIRST_USER.getRoles()));
    }
}
