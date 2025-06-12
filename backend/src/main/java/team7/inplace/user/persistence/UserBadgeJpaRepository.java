package team7.inplace.user.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import team7.inplace.user.domain.UserBadge;

public interface UserBadgeJpaRepository extends JpaRepository<UserBadge, Long> {

    List<UserBadge> findAllByUserId(Long userId);
}
