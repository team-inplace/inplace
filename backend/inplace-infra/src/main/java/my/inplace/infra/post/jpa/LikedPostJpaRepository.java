package my.inplace.infra.post.jpa;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import my.inplace.domain.post.LikedPost;

@Repository
public interface LikedPostJpaRepository extends JpaRepository<LikedPost, Long> {

    Optional<LikedPost> findByUserIdAndPostId(Long userId, Long postId);
    
    @Query("""
    SELECT lp.postId
    FROM LikedPost lp
    WHERE lp.userId = :userId
      AND lp.postId IN :postIds
      AND lp.isLiked = TRUE
    """)
    List<Long> findLikedPostIdsByUserIdAndPostIds(@Param("userId") Long userId, @Param("postIds") List<Long> postIds);
    
    @Query("""
    SELECT lp.postId
    FROM LikedPost lp
    WHERE lp.userId = :userId
      AND lp.isLiked = TRUE
    """)
    List<Long> findLikedPostIdsByUserId(@Param("userId") Long userId);
}
