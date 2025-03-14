package team7.inplace.video.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import team7.inplace.video.domain.CoolVideo;

public interface CoolVideoRepository extends JpaRepository<CoolVideo, Long> {

}
