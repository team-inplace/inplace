package team7.inplace.post.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team7.inplace.post.domain.Post;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Query("update Post p set p.totalLikeCount = p.totalLikeCount + 1 where p.id = :postId")
    void increaseLikeCount(Long postId);

    @Modifying
    @Query("update Post p set p.totalLikeCount = p.totalLikeCount - 1 where p.id = :postId")
    void decreaseLikeCount(Long postId);

    @Modifying
    @Query("update Post p set p.totalCommentCount = p.totalCommentCount + 1 where p.id = :postId")
    void increaseCommentCount(Long postId);

    @Modifying
    @Query("update Post p set p.totalCommentCount = p.totalCommentCount - 1 where p.id = :postId")
    void decreaseCommentCount(Long postId);

    @Query("SELECT p.content.content FROM Post p WHERE p.id = :postId")
    Optional<String> findContentById(@Param("postId") Long postId);
    
    @Query("SELECT p.authorId FROM Post p WHERE p.id = :postId")
    Optional<Long> findUserIdById(@Param("postId") Long postId);
    
    @Query("SELECT p.title FROM Post p WHERE p.id = :postId")
    Optional<String> findTitleById(@Param("postId") Long postId);

    List<Post> findAllByIsReportedTrueAndDeleteAtIsNull();

    @Query("SELECT p.authorId FROM Post p WHERE p.id = :postId")
    Long findAuthorIdByPostId(@Param("postId") Long postId);
}
