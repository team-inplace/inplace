package team7.inplace.post.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team7.inplace.post.domain.Post;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p.content.content FROM Post p WHERE p.id = :postId")
    Optional<String> findContentById(@Param("postId") Long postId);

}
