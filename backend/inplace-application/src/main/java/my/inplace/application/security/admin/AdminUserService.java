package my.inplace.application.security.admin;

import lombok.RequiredArgsConstructor;
import my.inplace.application.security.admin.dto.AdminCommand;
import my.inplace.application.security.admin.dto.AdminResult;
import my.inplace.domain.user.AdminUser;
import my.inplace.infra.user.jpa.AdminUserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminUserJpaRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<AdminResult.AuthInfo> findAdminUserByUsername(String username) {
        return adminUserRepository.findByUsername(username)
            .map(AdminResult.AuthInfo::from);
    }

    @Transactional
    public void registerAdminUser(AdminCommand.Register registerCommand) {
        AdminUser adminUser = new AdminUser(
            registerCommand.username(),
            passwordEncoder.encode(registerCommand.password())
        );
        adminUserRepository.save(adminUser);
    }
}
