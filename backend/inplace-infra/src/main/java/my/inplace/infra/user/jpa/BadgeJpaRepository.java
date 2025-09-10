package my.inplace.infra.user.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import my.inplace.domain.user.Badge;

public interface BadgeJpaRepository extends JpaRepository<Badge, Long> {

}
