package my.inplace.infra.post.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import my.inplace.domain.post.LikedComment;

public interface LikedCommentJpaRepository extends JpaRepository<LikedComment, Long> {

    Optional<LikedComment> findByUserIdAndCommentId(Long userId, Long commentId);
}
