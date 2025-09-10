package my.inplace.infra.user.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import my.inplace.domain.user.AdminUser;

@Repository
public interface AdminUserJpaRepository extends JpaRepository<AdminUser, Long> {

    Optional<AdminUser> findByUsername(String username);
}
