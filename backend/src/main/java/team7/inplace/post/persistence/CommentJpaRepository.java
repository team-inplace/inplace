package team7.inplace.post.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team7.inplace.post.domain.Comment;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query("UPDATE Comment c SET c.totalLikeCount = c.totalLikeCount + 1 WHERE c.id = :commentId")
    void increaseLikeCount(Long commentId);

    @Modifying
    @Query("UPDATE Comment c SET c.totalLikeCount = c.totalLikeCount - 1 WHERE c.id = :commentId")
    void decreaseLikeCount(Long commentId);

    @Query("SELECT c.content FROM Comment c WHERE c.id = :commentId")
    Optional<String> findContentById(@Param("commentId") Long commentId);

    List<Comment> findAllByIsReportedTrueAndDeleteAtIsNull();
}
