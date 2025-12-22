package my.inplace.security.application;

import lombok.RequiredArgsConstructor;
import my.inplace.domain.user.AdminUser;
import my.inplace.security.application.dto.AdminCommand;
import my.inplace.security.application.dto.AdminResult;
import my.inplace.infra.user.jpa.AdminUserJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        adminUserRepository.save(
            new AdminUser(
                registerCommand.username(),
                passwordEncoder.encode(registerCommand.password())
        ));
    }
}
