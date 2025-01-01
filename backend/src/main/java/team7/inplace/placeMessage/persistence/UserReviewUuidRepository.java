package team7.inplace.placeMessage.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team7.inplace.placeMessage.domain.UserReviewUuid;
import team7.inplace.user.domain.User;

public interface UserReviewUuidRepository extends JpaRepository<UserReviewUuid, Long> {

    @Query("SELECT ur.user FROM UserReviewUuid ur WHERE ur.uuid = :uuid AND ur.placeId = :placeId")
    Optional<User> findUserByUuidAndPlaceId(@Param("uuid") String uuid,
        @Param("placeId") Long placeId);

    Optional<UserReviewUuid> findByUserIdAndPlaceId(Long userId, Long placeId);
}