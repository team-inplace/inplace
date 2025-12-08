package my.inplace.infra.region.jpa;

import my.inplace.domain.region.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionJpaRepository extends JpaRepository<Region, Long> {
}
