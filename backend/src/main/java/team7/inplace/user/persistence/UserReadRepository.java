package team7.inplace.user.persistence;

import java.util.List;
import java.util.Optional;
import team7.inplace.user.persistence.dto.UserQueryResult.Badge;
import team7.inplace.user.persistence.dto.UserQueryResult.Simple;

public interface UserReadRepository {

    Optional<Simple> findUserInfoById(Long id);

    List<Badge> findAllBadgeByUserId(Long userId);
}
