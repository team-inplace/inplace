package my.inplace.infra.place.jpa;

import java.util.Optional;
import my.inplace.domain.place.LikedPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikedPlaceJpaRepository extends JpaRepository<LikedPlace, Long> {

    @Query("SELECT lp FROM LikedPlace lp WHERE lp.userId = :userId AND lp.placeId = :placeId AND lp.deleteAt IS NULL")
    Optional<LikedPlace> findByUserIdAndPlaceId(@Param("userId") Long userId, @Param("placeId") Long placeId);
}
