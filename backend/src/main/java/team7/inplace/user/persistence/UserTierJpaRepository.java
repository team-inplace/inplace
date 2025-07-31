package team7.inplace.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import team7.inplace.user.domain.UserTier;

public interface UserTierJpaRepository extends JpaRepository<UserTier, Long> {

}
