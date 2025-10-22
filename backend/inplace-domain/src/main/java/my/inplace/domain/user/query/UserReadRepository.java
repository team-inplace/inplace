package my.inplace.domain.user.query;

import java.util.List;
import java.util.Optional;
import my.inplace.domain.user.query.UserQueryResult.Badge;
import my.inplace.domain.user.query.UserQueryResult.BadgeWithOwnerShip;
import my.inplace.domain.user.query.UserQueryResult.Info;

public interface UserReadRepository {

    Optional<Info> findUserInfoById(Long id);

    List<Badge> findAllBadgeByUserId(Long userId);

    List<BadgeWithOwnerShip> getAllBadgesWithOwnerShip(Long userId);
}
