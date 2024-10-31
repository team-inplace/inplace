package team7.inplace.review.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import team7.inplace.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);
}
