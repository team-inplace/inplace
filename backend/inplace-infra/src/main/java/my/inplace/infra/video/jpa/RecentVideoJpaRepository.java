package my.inplace.infra.video.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import my.inplace.domain.video.RecentVideo;

public interface RecentVideoJpaRepository extends JpaRepository<RecentVideo, Long> {

}
