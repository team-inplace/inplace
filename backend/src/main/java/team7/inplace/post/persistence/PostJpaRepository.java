package team7.inplace.post.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
}
