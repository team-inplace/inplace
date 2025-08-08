package user.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import user.User;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.deleteAt IS NULL")
    Optional<User> findByUsername(String username);

    @Query("SELECT u.id FROM User u WHERE u.nickname = :nickname")
    Optional<Long> findIdByNickname(String nickname);

    boolean existsByUsername(String username);
}
