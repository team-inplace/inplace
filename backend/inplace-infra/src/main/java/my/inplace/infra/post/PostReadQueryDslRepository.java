package my.inplace.infra.post;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.inplace.common.cursor.CursorResult;
import my.inplace.domain.post.QComment;
import my.inplace.domain.post.QLikedPost;
import my.inplace.domain.post.QPost;
import my.inplace.domain.post.query.PostQueryResult;
import my.inplace.domain.post.query.PostQueryResult.CursorDetailedPost;
import my.inplace.domain.post.query.PostQueryResult.DetailedPost;
import my.inplace.domain.post.query.PostQueryResult.UserSuggestion;
import my.inplace.domain.post.query.PostReadRepository;
import my.inplace.domain.user.QBadge;
import my.inplace.domain.user.QTier;
import my.inplace.domain.user.QUser;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostReadQueryDslRepository implements PostReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public CursorResult<PostQueryResult.DetailedPost> findPostsOrderBy(
        Long userId,
        Long cursorValue,
        Long cursorId,
        int size,
        String orderBy
    ) {
        var orderedPostIds = queryFactory.select(QPost.post.id)
            .from(QPost.post)
            .where(
                QPost.post.deleteAt.isNull(),
                cursorId == null ? null : getCursorWhere(cursorValue, cursorId, orderBy)
            )
            .orderBy(getOrderSpecifier(orderBy))
            .limit(size + 1)
            .fetch();

        if (orderedPostIds.isEmpty()) {
            return new CursorResult<>(
                Collections.emptyList(),
                false,
                null,
                null
            );
        }

        var cursorPosts = buildCursorDetailedPostsQuery(userId, orderBy, orderedPostIds)
            .fetch();

        var sortedPosts = cursorPosts.stream()
            .sorted(Comparator.comparingInt(p -> orderedPostIds.indexOf(p.detailedPost().postId())))
            .toList();

        boolean hasNext = sortedPosts.size() > size;
        var posts = sortedPosts.stream().map(CursorDetailedPost::detailedPost).toList();

        return new CursorResult<>(
            posts.subList(0, Math.min(size, posts.size())),
            hasNext,
            hasNext ? sortedPosts.get(size - 1).cursorValue() : null,
            hasNext ? sortedPosts.get(size - 1).cursorId() : null
        );
    }

    @Override
    public Optional<DetailedPost> findPostById(Long postId, Long userId) {
        return Optional.ofNullable(buildDetailedPostsQuery(postId, userId).fetchOne());
    }

    private JPAQuery<CursorDetailedPost> buildCursorDetailedPostsQuery(
        Long userId,
        String orderBy,
        List<Long> orderedPostIds
    ) {
        var query = queryFactory
            .select(Projections.constructor(PostQueryResult.CursorDetailedPost.class,
                    Projections.constructor(PostQueryResult.DetailedPost.class,
                        QPost.post.id,
                        QUser.user.nickname,
                        QUser.user.profileImageUrl,
                        QTier.tier.imgUrl,
                        QBadge.badge.imgUrl,
                        QPost.post.title.title,
                        QPost.post.content.content.substring(0, 120),
                        QPost.post.photos.imageInfos,
                        userId == null
                            ? Expressions.FALSE
                            : QLikedPost.likedPost.isLiked.coalesce(false),
                        QPost.post.totalLikeCount,
                        QPost.post.totalCommentCount,
                        userId == null ? Expressions.FALSE : QPost.post.authorId.eq(userId),
                        QPost.post.createdAt
                    ),
                    getCursorPath(orderBy),
                    QPost.post.id
                )
            )
            .from(QPost.post)
            .leftJoin(QUser.user).on(QPost.post.authorId.eq(QUser.user.id))
            .leftJoin(QTier.tier).on(QUser.user.tierId.eq(QTier.tier.id))
            .leftJoin(QBadge.badge).on(QUser.user.mainBadgeId.eq(QBadge.badge.id))
            .where(QPost.post.id.in(orderedPostIds));

        if (userId != null) {
            query.leftJoin(QLikedPost.likedPost)
                .on(QLikedPost.likedPost.postId.eq(QPost.post.id)
                    .and(QLikedPost.likedPost.userId.eq(userId)));
        }

        return query;
    }

    private OrderSpecifier<?>[] getOrderSpecifier(String orderBy) {
        return switch (orderBy) {
            case "popularity" -> new OrderSpecifier<?>[]{
                QPost.post.totalLikeCount.desc(),
                QPost.post.id.desc()
            };
            default -> new OrderSpecifier<?>[]{ QPost.post.id.desc() };
        };
    }

    private NumberExpression<Long> getCursorPath(String orderBy) {
        return switch (orderBy) {
            case "popularity" -> QPost.post.totalLikeCount.longValue();
            default -> QPost.post.id;
        };
    }

    private BooleanExpression getCursorWhere(Long cursorValue, Long cursorId, String orderBy) {
        return switch (orderBy) {
            case "popularity" -> QPost.post.totalLikeCount.longValue().lt(cursorValue)
                .or(QPost.post.totalLikeCount.longValue().eq(cursorValue).and(QPost.post.id.lt(cursorId)));
            default -> QPost.post.id.lt(cursorId);
        };
    }

    private JPAQuery<DetailedPost> buildDetailedPostsQuery(Long postId, Long userId) {
        var query = queryFactory
            .select(
                Projections.constructor(PostQueryResult.DetailedPost.class,
                    QPost.post.id,
                    QUser.user.nickname,
                    QUser.user.profileImageUrl,
                    QTier.tier.imgUrl,
                    QBadge.badge.imgUrl,
                    QPost.post.title.title,
                    QPost.post.content.content,
                    QPost.post.photos.imageInfos,
                    userId == null
                        ? Expressions.FALSE
                        : QLikedPost.likedPost.isLiked.coalesce(false),
                    QPost.post.totalLikeCount,
                    QPost.post.totalCommentCount,
                    userId == null ? Expressions.FALSE : QPost.post.authorId.eq(userId),
                    QPost.post.createdAt
                )
            )
            .from(QPost.post)
            .innerJoin(QUser.user).on(QPost.post.authorId.eq(QUser.user.id))
            .innerJoin(QTier.tier).on(QUser.user.tierId.eq(QTier.tier.id))
            .leftJoin(QBadge.badge).on(QUser.user.mainBadgeId.eq(QBadge.badge.id))
            .where(QPost.post.id.eq(postId)
                .and(QPost.post.deleteAt.isNull())
            );

        if (userId != null) {
            query.leftJoin(QLikedPost.likedPost)
                .on(QLikedPost.likedPost.postId.eq(QPost.post.id)
                    .and(QLikedPost.likedPost.userId.eq(userId)));
        }

        return query;
    }

    @Override
    public List<UserSuggestion> findCommentUserSuggestions(
        Long postId, String keyword
    ) {
        var postUser = queryFactory
            .select(Projections.constructor(PostQueryResult.UserSuggestion.class,
                QUser.user.id,
                QUser.user.nickname,
                QUser.user.profileImageUrl
            ))
            .from(QPost.post)
            .innerJoin(QUser.user).on(QPost.post.authorId.eq(QUser.user.id))
            .where(
                QPost.post.id.eq(postId),
                keyword == null || keyword.isEmpty()
                    ? null
                    : QUser.user.nickname.containsIgnoreCase(keyword)
            )
            .fetch();

        var commentUser = queryFactory
            .select(Projections.constructor(PostQueryResult.UserSuggestion.class,
                QUser.user.id,
                QUser.user.nickname,
                QUser.user.profileImageUrl
            ))
            .from(QUser.user)
            .where(
                keyword == null || keyword.isEmpty()
                    ? null
                    : QUser.user.nickname.containsIgnoreCase(keyword),
                JPAExpressions
                    .selectOne()
                    .from(QComment.comment)
                    .where(QComment.comment.postId.eq(postId)
                        .and(QComment.comment.authorId.eq(QUser.user.id)))
                    .exists()
            )
            .fetch();

        return Stream.concat(postUser.stream(), commentUser.stream()).distinct().toList();
    }
}
