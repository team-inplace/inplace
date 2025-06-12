package team7.inplace.post.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team7.inplace.post.domain.Comment;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

}
