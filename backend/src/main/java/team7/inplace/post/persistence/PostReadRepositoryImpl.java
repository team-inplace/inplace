package team7.inplace.post.persistence;

import base.CursorResult;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import post.query.PostQueryResult;
import post.query.PostQueryResult.CursorDetailedPost;
import post.query.PostQueryResult.DetailedPost;
import post.query.PostQueryResult.UserSuggestion;
import post.query.PostReadRepository;

@Repository
@RequiredArgsConstructor
public class PostReadRepositoryImpl implements PostReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public CursorResult<PostQueryResult.DetailedPost> findPostsOrderBy(
        Long userId,
        Long cursorId,
        int size,
        String orderBy
    ) {
        var cursorPosts = buildCursorDetailedPostsQuery(userId, orderBy)
            .where(cursorId == null ? null : getCursorWhere(cursorId, orderBy))
            .orderBy(getOrderSpecifier(orderBy))
            .limit(size + 1)
            .fetch();

        boolean hasNext = cursorPosts.size() > size;
        var posts = cursorPosts.stream().map(CursorDetailedPost::detailedPost).toList();
        return new CursorResult<>(
            posts.subList(0, Math.min(size, posts.size())),
            hasNext,
            hasNext ? cursorPosts.get(size - 1).cursorId() : null
        );

    }

    @Override
    public Optional<DetailedPost> findPostById(Long postId, Long userId) {
        return Optional.ofNullable(buildDetailedPostsQuery(postId, userId).fetchOne());
    }

    private JPAQuery<CursorDetailedPost> buildCursorDetailedPostsQuery(
        Long userId,
        String orderBy
    ) {
        var likedJoinCondition = QLikedPost.likedPost.postId.eq(QPost.post.id);
        if (userId != null) {
            likedJoinCondition = likedJoinCondition.and(QLikedPost.likedPost.userId.eq(userId));
        }
        return queryFactory
            .select(new QPostQueryResult_CursorDetailedPost(
                    new QPostQueryResult_DetailedPost(
                        QPost.post.id,
                        QUser.user.nickname,
                        QUser.user.profileImageUrl,
                        QUserTier.userTier.imgUrl,
                        QBadge.badge.imgUrl,
                        QPost.post.title.title,
                        QPost.post.content.content.substring(0, 120),
                        QPost.post.photos.imageInfos,
                        QLikedPost.likedPost.id.isNotNull(),
                        QPost.post.totalLikeCount,
                        QPost.post.totalCommentCount,
                        userId == null ? Expressions.FALSE : QPost.post.authorId.eq(userId),
                        QPost.post.createdAt
                    ),
                    getCursorPath(orderBy)
                )
            )
            .from(QPost.post)
            .innerJoin(QUser.user).on(QPost.post.authorId.eq(QUser.user.id))
            .innerJoin(QUserTier.userTier).on(QUser.user.tierId.eq(QUserTier.userTier.id))
            .leftJoin(QBadge.badge).on(QUser.user.mainBadgeId.eq(QBadge.badge.id))
            .leftJoin(QLikedPost.likedPost).on(likedJoinCondition)
            .where(QPost.post.deleteAt.isNull());
    }

    private JPAQuery<DetailedPost> buildDetailedPostsQuery(Long postId, Long userId) {
        var likedJoinCondition = QLikedPost.likedPost.postId.eq(QPost.post.id);
        if (userId != null) {
            likedJoinCondition = likedJoinCondition.and(QLikedPost.likedPost.userId.eq(userId));
        }
        return queryFactory
            .select(
                new QPostQueryResult_DetailedPost(
                    QPost.post.id,
                    QUser.user.nickname,
                    QUser.user.profileImageUrl,
                    QUserTier.userTier.imgUrl,
                    QBadge.badge.imgUrl,
                    QPost.post.title.title,
                    QPost.post.content.content,
                    QPost.post.photos.imageInfos,
                    QLikedPost.likedPost.id.isNotNull(),
                    QPost.post.totalLikeCount,
                    QPost.post.totalCommentCount,
                    userId == null ? Expressions.FALSE : QPost.post.authorId.eq(userId),
                    QPost.post.createdAt
                )
            )
            .from(QPost.post)
            .innerJoin(QUser.user).on(QPost.post.authorId.eq(QUser.user.id))
            .innerJoin(QUserTier.userTier).on(QUser.user.tierId.eq(QUserTier.userTier.id))
            .leftJoin(QBadge.badge).on(QUser.user.mainBadgeId.eq(QBadge.badge.id))
            .leftJoin(QLikedPost.likedPost).on(likedJoinCondition)
            .where(QPost.post.id.eq(postId)
                .and(QPost.post.deleteAt.isNull())
            );
    }

    /*
     * 추천수 기준 정렬 추가 예정입니다.
     */

    private OrderSpecifier<?> getOrderSpecifier(String orderBy) {
        return switch (orderBy) {
            default -> QPost.post.id.desc();
        };
    }

    private NumberPath<Long> getCursorPath(String orderBy) {
        return switch (orderBy) {
            default -> QPost.post.id;
        };
    }

    private BooleanExpression getCursorWhere(Long cursorId, String orderBy) {
        return switch (orderBy) {
            default -> QPost.post.id.lt(cursorId);
        };
    }

    @Override
    public List<UserSuggestion> findCommentUserSuggestions(
        Long postId, String keyword
    ) {
        var postUser = queryFactory
            .select(new QPostQueryResult_UserSuggestion(
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
            .select(new QPostQueryResult_UserSuggestion(
                QUser.user.id,
                QUser.user.nickname,
                QUser.user.profileImageUrl
            )).distinct()
            .from(QComment.comment)
            .innerJoin(QUser.user).on(QComment.comment.authorId.eq(QUser.user.id))
            .where(
                QComment.comment.postId.eq(postId),
                keyword == null || keyword.isEmpty()
                    ? null
                    : QUser.user.nickname.containsIgnoreCase(keyword)
            )
            .fetch();

        return Stream.concat(postUser.stream(), commentUser.stream()).distinct().toList();
    }
}
