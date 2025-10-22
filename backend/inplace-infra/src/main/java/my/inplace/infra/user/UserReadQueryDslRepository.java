package my.inplace.infra.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.user.QBadge;
import my.inplace.domain.user.QTier;
import my.inplace.domain.user.QUser;
import my.inplace.domain.user.QUserBadge;
import my.inplace.domain.user.query.UserQueryResult;
import my.inplace.domain.user.query.UserQueryResult.Badge;
import my.inplace.domain.user.query.UserQueryResult.BadgeWithOwnerShip;
import my.inplace.domain.user.query.UserQueryResult.Info;
import my.inplace.domain.user.query.UserReadRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserReadQueryDslRepository implements UserReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Info> findUserInfoById(Long id) {
        Info userInfo = queryFactory
            .select(Projections.constructor(Info.class,
                QUser.user.nickname,
                QUser.user.profileImageUrl,
                QTier.tier.name,
                QTier.tier.imgUrl,
                QBadge.badge.name,
                QBadge.badge.imgUrl
            ))
            .from(QUser.user)
            .innerJoin(QTier.tier).on(QTier.tier.id.eq(QUser.user.tierId))
            .leftJoin(QBadge.badge).on(QBadge.badge.id.eq(QUser.user.mainBadgeId))
            .where(QUser.user.id.eq(id))
            .fetchOne();

        return Optional.ofNullable(userInfo);
    }

    @Override
    public List<BadgeWithOwnerShip> getAllBadgesWithOwnerShip(Long userId) {
        Long mainBadgeId = queryFactory
            .select(QUser.user.mainBadgeId)
            .from(QUser.user)
            .where(QUser.user.id.eq(userId))
            .fetchOne();

        return queryFactory
            .select(Projections.constructor(UserQueryResult.BadgeWithOwnerShip.class,
            QBadge.badge.id,
            QBadge.badge.name,
            QBadge.badge.imgUrl,
            QBadge.badge.condition,
            QUserBadge.userBadge.id.isNotNull(),
            QBadge.badge.id.eq(mainBadgeId)
            ))
            .from(QBadge.badge)
            .leftJoin(QUserBadge.userBadge).on(QUserBadge.userBadge.badgeId.eq(QBadge.badge.id)
                .and(QUserBadge.userBadge.userId.eq(userId)))
            .fetch();
    }

}
