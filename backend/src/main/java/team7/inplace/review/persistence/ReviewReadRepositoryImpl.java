package team7.inplace.review.persistence;

import static com.querydsl.core.types.ExpressionUtils.count;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import team7.inplace.place.domain.QPlace;
import team7.inplace.review.domain.QReview;
import team7.inplace.review.persistence.dto.QReviewQueryResult_Detail;
import team7.inplace.review.persistence.dto.QReviewQueryResult_LikeRate;
import team7.inplace.review.persistence.dto.QReviewQueryResult_Simple;
import team7.inplace.review.persistence.dto.ReviewQueryResult;
import team7.inplace.user.domain.QUser;
import team7.inplace.video.domain.QVideo;

@Repository
@RequiredArgsConstructor
public class ReviewReadRepositoryImpl implements ReviewReadRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ReviewQueryResult.Detail> findDetailedReviewByUserId(
        Long userId,
        Pageable pageable
    ) {
        var total = jpaQueryFactory
            .select(count(QReview.review.id))
            .from(QReview.review)
            .where(
                QReview.review.userId.eq(userId),
                QReview.review.deleteAt.isNull()
            )
            .fetchOne();

        if (total == null || total == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        var contents = jpaQueryFactory
            .select(new QReviewQueryResult_Detail(
                QReview.review.id,
                QReview.review.isLiked,
                QReview.review.comment,
                QReview.review.createdDate,
                QPlace.place.id,
                QPlace.place.name,
                QPlace.place.address.address1,
                QPlace.place.address.address2,
                QPlace.place.address.address3))
            .from(QReview.review)
            .innerJoin(QPlace.place).on(QReview.review.placeId.eq(QPlace.place.id))
            .where(
                QReview.review.userId.eq(userId),
                QReview.review.deleteAt.isNull(),
                QPlace.place.deleteAt.isNull()
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(contents, pageable, total);
    }

    @Override
    public Page<ReviewQueryResult.Simple> findSimpleReviewByUserIdAndPlaceId(
        Long placeId, Long userId, Pageable pageable
    ) {
        var totalCount = jpaQueryFactory
            .select(count(QReview.review.id))
            .from(QReview.review)
            .where(
                QReview.review.placeId.eq(placeId),
                QReview.review.deleteAt.isNull()
            )
            .fetchOne();
        if (totalCount == null || totalCount == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        var reviews = jpaQueryFactory
            .select(new QReviewQueryResult_Simple(
                QReview.review.id,
                QReview.review.isLiked,
                QReview.review.comment,
                QUser.user.nickname,
                QReview.review.createdDate,
                userId == null ? Expressions.FALSE : QReview.review.userId.eq(userId)
            ))
            .from(QReview.review)
            .innerJoin(QUser.user).on(QReview.review.userId.eq(QUser.user.id))
            .where(
                QReview.review.placeId.eq(placeId),
                QReview.review.deleteAt.isNull(),
                QUser.user.deleteAt.isNull()
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(reviews, pageable, totalCount);
    }

    @Override
    public ReviewQueryResult.LikeRate countRateByPlaceId(Long placeId) {
        var result = jpaQueryFactory
            .select(new QReviewQueryResult_LikeRate(
                QReview.review.isLiked.when(true).then(1L).otherwise(0L).sum().coalesce(0L),
                QReview.review.isLiked.when(false).then(1L).otherwise(0L).sum().coalesce(0L)
            ))
            .from(QReview.review)
            .where(
                QReview.review.placeId.eq(placeId),
                QReview.review.deleteAt.isNull()
            )
            .fetchOne();

        return new ReviewQueryResult.LikeRate(result.likes(), result.dislikes());
    }
}
