package my.inplace.infra.post;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.post.QComment;
import my.inplace.domain.post.QLikedComment;
import my.inplace.domain.post.query.CommentQueryResult;
import my.inplace.domain.post.query.CommentReadRepository;
import my.inplace.domain.user.QBadge;
import my.inplace.domain.user.QTier;
import my.inplace.domain.user.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentReadQueryDslRepository implements CommentReadRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CommentQueryResult.DetailedComment> findCommentsByPostId(
        Long postId, Long userId, Pageable pageable
    ) {
        Long total = jpaQueryFactory
            .select(QComment.comment.count())
            .from(QComment.comment)
            .where(QComment.comment.postId.eq(postId))
            .fetchOne();
        if (total == null || total == 0) {
            return Page.empty(pageable);
        }
        var query = buildDetailedCommentQuery(postId, userId)
            .orderBy(QComment.comment.id.asc()) // 오래된 순
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return PageableExecutionUtils.getPage(query, pageable, () -> total);
    }


    private JPAQuery<CommentQueryResult.DetailedComment> buildDetailedCommentQuery(
        Long postId,
        Long userId
    ) {
        var query = jpaQueryFactory.select(
                Projections.constructor(CommentQueryResult.DetailedComment.class,
                    QComment.comment.id,
                    QUser.user.nickname,
                    QUser.user.profileImageUrl,
                    QTier.tier.imgUrl,
                    QBadge.badge.imgUrl,
                    QComment.comment.content,
                    userId == null
                        ? Expressions.FALSE
                        : QLikedComment.likedComment.isLiked.coalesce(false),
                    QComment.comment.totalLikeCount,
                    userId == null
                        ? Expressions.FALSE
                        : QComment.comment.authorId.eq(userId),
                    QComment.comment.createdAt
                )
            )
            .from(QComment.comment)
            .innerJoin(QUser.user).on(QComment.comment.authorId.eq(QUser.user.id))
            .innerJoin(QTier.tier).on(QUser.user.tierId.eq(QTier.tier.id))
            .leftJoin(QBadge.badge).on(QUser.user.mainBadgeId.eq(QBadge.badge.id))
            .where(QComment.comment.postId.eq(postId).and(QComment.comment.deleteAt.isNull()));

        if (userId != null) {
            query.leftJoin(QLikedComment.likedComment)
                .on(QLikedComment.likedComment.commentId.eq(QComment.comment.id)
                    .and(QLikedComment.likedComment.userId.eq(userId)));
        }

        return query;
    }
}
