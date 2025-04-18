package team7.inplace.admin.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team7.inplace.global.baseEntity.BaseEntity;
import team7.inplace.user.domain.Role;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "admin_users")
public class AdminUser extends BaseEntity {
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public AdminUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = Role.USER;
    }
}
