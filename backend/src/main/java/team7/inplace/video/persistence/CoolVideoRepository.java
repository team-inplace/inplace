package team7.inplace.video.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import team7.inplace.video.domain.CoolVideo;

public interface CoolVideoRepository extends JpaRepository<CoolVideo, Long> {

    List<CoolVideo> findByPlaceCategoryParentName(String parentCategoryName);
}
