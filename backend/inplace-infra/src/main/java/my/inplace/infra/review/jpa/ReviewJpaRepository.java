package my.inplace.infra.review.jpa;

import my.inplace.domain.review.Review;
import my.inplace.domain.review.query.ReviewQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
        "FROM Review r WHERE r.userId = :userId AND r.placeId = :placeId AND r.deleteAt IS NULL")
    boolean existsByUserIdAndPlaceId(@Param("userId") Long userId, @Param("placeId") Long placeId);

    @Query("SELECT r FROM Review r WHERE r.placeId = :placeId AND r.deleteAt IS NULL")
    Page<Review> findByPlaceId(@Param("placeId") Long placeId, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.userId = :userId AND r.deleteAt IS NULL")
    Page<Review> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(
        "SELECT new my.inplace.domain.review.query.ReviewQueryResult$LikeRate( " + // <--- 이렇게 수정!
            "COALESCE(SUM(CASE WHEN r.isLiked = true THEN 1 ELSE 0 END), 0L), " +
            "COALESCE(SUM(CASE WHEN r.isLiked = false THEN 1 ELSE 0 END), 0L)) " +
            "FROM Review r " +
            "WHERE r.placeId = :placeId AND r.deleteAt IS NULL")
    ReviewQueryResult.LikeRate countRateByPlaceId(@Param("placeId") Long placeId);
}
