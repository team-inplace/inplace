package team7.inplace.user.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team7.inplace.user.domain.QBadge;
import team7.inplace.user.domain.QUser;
import team7.inplace.user.domain.QUserBadge;
import team7.inplace.user.domain.QUserTier;
import team7.inplace.user.persistence.dto.QUserQueryResult_Badge;
import team7.inplace.user.persistence.dto.QUserQueryResult_Simple;
import team7.inplace.user.persistence.dto.UserQueryResult.Badge;
import team7.inplace.user.persistence.dto.UserQueryResult.Simple;

@Repository
@RequiredArgsConstructor
public class UserReadRepositoryImpl implements UserReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Simple> findUserInfoById(Long id) {
        Simple userSimple = queryFactory
            .select(new QUserQueryResult_Simple(
                QUser.user.nickname,
                QUser.user.profileImageUrl,
                QUserTier.userTier.name,
                QUserTier.userTier.imgUrl,
                QBadge.badge.name,
                QBadge.badge.imgUrl
            ))
            .from(QUser.user)
            .innerJoin(QUserTier.userTier).on(QUserTier.userTier.id.eq(QUser.user.tierId))
            .leftJoin(QBadge.badge).on(QBadge.badge.id.eq(QUser.user.mainBadgeId))
            .where(QUser.user.id.eq(id))
            .fetchOne();

        return Optional.ofNullable(userSimple);
    }

    @Override
    public List<Badge> findAllBadgeByUserId(Long userId) {
        return queryFactory
            .select(new QUserQueryResult_Badge(
                QBadge.badge.id,
                QBadge.badge.name,
                QBadge.badge.imgUrl,
                QBadge.badge.condition
            ))
            .from(QBadge.badge)
            .leftJoin(QUserBadge.userBadge).on(QUserBadge.userBadge.badgeId.eq(QBadge.badge.id))
            .where(QUserBadge.userBadge.userId.eq(userId))
            .fetch();
    }

}
