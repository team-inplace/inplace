package my.inplace.infra.security.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import my.inplace.domain.security.OauthToken;

@Repository
public interface OauthTokenJpaRepository extends JpaRepository<OauthToken, Long> {

    Optional<OauthToken> findByUserId(Long userId);
}
