package user.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import user.Badge;

public interface BadgeJpaRepository extends JpaRepository<Badge, Long> {

}
