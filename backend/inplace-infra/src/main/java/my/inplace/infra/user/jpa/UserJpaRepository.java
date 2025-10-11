package my.inplace.infra.user.jpa;

import java.util.Optional;
import my.inplace.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.deleteAt IS NULL")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u.id FROM User u WHERE u.nickname = :nickname")
    Optional<Long> findIdByNickname(@Param("nickname") String nickname);

    boolean existsByUsername(String username);
}
