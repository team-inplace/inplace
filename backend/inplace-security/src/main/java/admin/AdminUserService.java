package admin;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.AdminUser;
import user.jpa.AdminUserJpaRepository;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserJpaRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<AdminUserResult> findAdminUserByUsername(String username) {
        return adminUserRepository.findByUsername(username)
            .map(AdminUserResult::from);
    }

    @Transactional
    public void registerAdminUser(AdminUserCommand registerCommand) {
        AdminUser adminUser = new AdminUser(
            registerCommand.username(),
            passwordEncoder.encode(registerCommand.password())
        );
        adminUserRepository.save(adminUser);
    }
}
