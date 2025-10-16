package my.inplace.infra.post.jpa;

import java.util.List;
import java.util.Optional;

import my.inplace.domain.post.PostTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import my.inplace.domain.post.Post;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Query("update Post p set p.totalLikeCount = p.totalLikeCount + 1 where p.id = :postId")
    void increaseLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("update Post p set p.totalLikeCount = p.totalLikeCount - 1 where p.id = :postId")
    void decreaseLikeCount(@Param("postId") Long postId);

    @Modifying
    @Query("update Post p set p.totalCommentCount = p.totalCommentCount + 1 where p.id = :postId")
    void increaseCommentCount(@Param("postId") Long postId);

    @Modifying
    @Query("update Post p set p.totalCommentCount = p.totalCommentCount - 1 where p.id = :postId")
    void decreaseCommentCount(@Param("postId") Long postId);

    @Query("SELECT p.content.content FROM Post p WHERE p.id = :postId")
    Optional<String> findContentById(@Param("postId") Long postId);

    @Query("SELECT p.authorId FROM Post p WHERE p.id = :postId")
    Optional<Long> findUserIdById(@Param("postId") Long postId);

    @Query("SELECT p.title FROM Post p WHERE p.id = :postId")
    Optional<PostTitle> findTitleById(@Param("postId") Long postId);

    List<Post> findAllByIsReportedTrueAndDeleteAtIsNull();

    @Query("SELECT p.authorId FROM Post p WHERE p.id = :postId")
    Long findAuthorIdByPostId(@Param("postId") Long postId);
    
    @Query("""
        SELECT COUNT(c)
        FROM Comment c
        WHERE c.postId = :postId
        AND c.createdAt < (SELECT c2.createdAt FROM Comment c2 WHERE c2.id = :commentId)
    """)
    long findIndexOfComment(@Param("postId") Long postId, @Param("commentId") Long commentId);
}
