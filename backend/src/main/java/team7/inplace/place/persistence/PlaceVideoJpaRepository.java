package team7.inplace.place.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team7.inplace.place.domain.PlaceVideo;

@Repository
public interface PlaceVideoJpaRepository extends JpaRepository<PlaceVideo, Long> {

}
