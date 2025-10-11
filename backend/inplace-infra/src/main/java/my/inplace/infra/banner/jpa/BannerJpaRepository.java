package my.inplace.infra.banner.jpa;

import java.time.LocalDateTime;
import java.util.List;

import my.inplace.domain.banner.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerJpaRepository extends JpaRepository<Banner, Long> {

    @Query("SELECT l FROM Banner l WHERE l.startDate <= :now AND l.endDate >= :now or l.isFixed = true")
    List<Banner> findActiveBanner(@Param("now") LocalDateTime now);
}
