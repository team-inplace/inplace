package team7.inplace.video.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import team7.inplace.video.domain.RecentVideo;

public interface RecentVideoRepository extends JpaRepository<RecentVideo, Long> {

}
