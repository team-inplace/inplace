package team7.inplace.LikedPlace.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import team7.inplace.LikedPlace.domain.LikedPlace;

public interface LikedPlaceRepository extends JpaRepository<LikedPlace, Long> {

    Optional<LikedPlace> findByUserIdAndPlaceId(Long userId, Long placeId);
}
