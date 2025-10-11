package my.inplace.infra.user.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import my.inplace.domain.user.Tier;

public interface TierJpaRepository extends JpaRepository<Tier, Long> {

}
