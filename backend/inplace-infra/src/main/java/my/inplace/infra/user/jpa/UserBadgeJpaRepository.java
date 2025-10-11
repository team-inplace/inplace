package my.inplace.infra.user.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import my.inplace.domain.user.UserBadge;

public interface UserBadgeJpaRepository extends JpaRepository<UserBadge, Long> {

    List<UserBadge> findAllByUserId(Long userId);
}
