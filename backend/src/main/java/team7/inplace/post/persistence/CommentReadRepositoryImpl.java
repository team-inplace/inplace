package team7.inplace.post.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import team7.inplace.liked.likedComment.domain.QLikedComment;
import team7.inplace.post.domain.QComment;
import team7.inplace.post.persistence.dto.CommentQueryResult;
import team7.inplace.post.persistence.dto.QCommentQueryResult_DetailedComment;
import team7.inplace.user.domain.QBadge;
import team7.inplace.user.domain.QUser;
import team7.inplace.user.domain.QUserTier;

@Repository
@RequiredArgsConstructor
public class CommentReadRepositoryImpl implements CommentReadRepository {

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
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return PageableExecutionUtils.getPage(query, pageable, () -> total);
    }


    private JPAQuery<CommentQueryResult.DetailedComment> buildDetailedCommentQuery(
        Long postId,
        Long userId
    ) {
        BooleanExpression likedJoinCondition = QComment.comment.id.eq(
            QLikedComment.likedComment.commentId);

        if (userId != null) {
            likedJoinCondition = likedJoinCondition.and(
                QLikedComment.likedComment.userId.eq(userId));
        }

        return jpaQueryFactory.select(
                new QCommentQueryResult_DetailedComment(
                    QComment.comment.id,
                    QUser.user.nickname,
                    QUser.user.profileImageUrl,
                    QUserTier.userTier.imgUrl,
                    QBadge.badge.imgUrl,
                    QComment.comment.content,
                    userId == null ?
                        Expressions.constant(false) :
                        Expressions.cases()
                            .when(QLikedComment.likedComment.userId.eq(userId))
                            .then(true)
                            .otherwise(false),
                    QComment.comment.totalLikeCount,
                    userId == null ?
                        Expressions.constant(false) :
                        Expressions.cases()
                            .when(QComment.comment.authorId.eq(userId))
                            .then(true)
                            .otherwise(false),
                    QComment.comment.createdAt
                )
            )
            .from(QComment.comment)
            .join(QUser.user).on(QComment.comment.authorId.eq(QUser.user.id))
            .innerJoin(QUserTier.userTier).on(QUser.user.tierId.eq(QUserTier.userTier.id))
            .leftJoin(QBadge.badge).on(QUser.user.mainBadgeId.eq(QBadge.badge.id))
            .leftJoin(QLikedComment.likedComment)
            .on(userId == null ? Expressions.FALSE : likedJoinCondition)
            .where(QComment.comment.postId.eq(postId));
    }
}