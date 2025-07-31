package team7.inplace.user.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team7.inplace.user.domain.User;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.deleteAt IS NULL")
    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String username);
}
