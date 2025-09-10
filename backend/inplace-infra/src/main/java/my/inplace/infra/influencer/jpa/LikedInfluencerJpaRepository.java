package my.inplace.infra.influencer.jpa;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import my.inplace.domain.influencer.LikedInfluencer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikedInfluencerJpaRepository extends JpaRepository<LikedInfluencer, Long> {

    @Query("SELECT f FROM LikedInfluencer f WHERE f.userId = :userId AND f.deleteAt IS NULL")
    List<LikedInfluencer> findByUserId(@Param("userId") Long userId);

    @Query("SELECT f FROM LikedInfluencer f WHERE f.userId = :userId AND f.isLiked = true AND f.deleteAt IS NULL")
    Page<LikedInfluencer> findByUserIdAndIsLikedTrue(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT f FROM LikedInfluencer f WHERE f.userId = :userId AND f.influencerId = :influencerId AND f.deleteAt IS NULL")
    Optional<LikedInfluencer> findByUserIdAndInfluencerId(@Param("userId") Long userId, @Param("influencerId") Long influencerId);

    @Query("SELECT f.influencerId FROM LikedInfluencer f WHERE f.userId = :userId AND f.isLiked = true AND f.deleteAt IS NULL")
    Set<Long> findLikedInfluencerIdsByUserId(@Param("userId") Long userId);
}
