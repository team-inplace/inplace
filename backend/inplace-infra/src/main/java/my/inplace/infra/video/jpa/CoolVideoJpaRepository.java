package my.inplace.infra.video.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import my.inplace.domain.video.CoolVideo;

public interface CoolVideoJpaRepository extends JpaRepository<CoolVideo, Long> {

    List<CoolVideo> findByPlaceCategoryParentName(String parentCategoryName);
}
