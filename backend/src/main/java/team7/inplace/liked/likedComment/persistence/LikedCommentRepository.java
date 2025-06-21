package team7.inplace.liked.likedComment.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import team7.inplace.liked.likedComment.domain.LikedComment;

public interface LikedCommentRepository extends JpaRepository<LikedComment, Long> {

    Optional<LikedComment> findByUserIdAndCommentId(Long userId, Long commentId);
}
