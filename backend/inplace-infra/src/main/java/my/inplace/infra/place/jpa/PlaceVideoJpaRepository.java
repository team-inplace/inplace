package my.inplace.infra.place.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import my.inplace.domain.place.PlaceVideo;

@Repository
public interface PlaceVideoJpaRepository extends JpaRepository<PlaceVideo, Long> {

}
