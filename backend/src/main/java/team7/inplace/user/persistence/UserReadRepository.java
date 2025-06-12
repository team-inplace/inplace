package team7.inplace.user.persistence;

import java.util.List;
import java.util.Optional;
import team7.inplace.user.persistence.dto.UserQueryResult;
import team7.inplace.user.persistence.dto.UserQueryResult.Badge;

public interface UserReadRepository {

    Optional<UserQueryResult.Info> findUserInfoById(Long id);

    List<Badge> findAllBadgeByUserId(Long userId);
}
