package my.inplace.infra.post.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import my.inplace.domain.post.Comment;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query("UPDATE Comment c SET c.totalLikeCount = c.totalLikeCount + 1 WHERE c.id = :commentId")
    void increaseLikeCount(@Param("commentId") Long commentId);

    @Modifying
    @Query("UPDATE Comment c SET c.totalLikeCount = c.totalLikeCount - 1 WHERE c.id = :commentId")
    void decreaseLikeCount(@Param("commentId") Long commentId);

    @Query("SELECT c.content FROM Comment c WHERE c.id = :commentId")
    Optional<String> findContentById(@Param("commentId") Long commentId);

    @Query("SELECT c.authorId FROM Comment c WHERE c.id = :commentId")
    Optional<Long> findAuthorIdById(@Param("commentId") Long commentId);

    @Query("SELECT c.postId FROM Comment c WHERE c.id = :commentId")
    Optional<Long> findPostIdById(@Param("commentId") Long commentId);

    List<Comment> findAllByIsReportedTrueAndDeleteAtIsNull();

    Boolean existsCommentByPostIdAndId(Long postId, Long commentId);
    
    @Query("SELECT c.id FROM Comment c WHERE c.authorId = :userId")
    List<Long> findCommentIdsByUserId(@Param("userId") Long userId);
}
