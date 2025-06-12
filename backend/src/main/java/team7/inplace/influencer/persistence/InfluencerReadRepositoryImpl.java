package team7.inplace.influencer.persistence;

import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.jpa.JPAExpressions.select;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import team7.inplace.influencer.domain.QInfluencer;
import team7.inplace.influencer.persistence.dto.InfluencerQueryResult;
import team7.inplace.influencer.persistence.dto.InfluencerQueryResult.Simple;
import team7.inplace.influencer.persistence.dto.QInfluencerQueryResult_Detail;
import team7.inplace.influencer.persistence.dto.QInfluencerQueryResult_Simple;
import team7.inplace.liked.likedInfluencer.domain.QLikedInfluencer;
import team7.inplace.place.domain.QPlaceVideo;
import team7.inplace.video.domain.QVideo;

@Repository
@RequiredArgsConstructor
public class InfluencerReadRepositoryImpl implements InfluencerReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<InfluencerQueryResult.Detail> getInfluencerDetail(
        Long influencerId,
        Long userId
    ) {
        boolean exists = queryFactory
            .selectFrom(QInfluencer.influencer)
            .where(QInfluencer.influencer.id.eq(influencerId))
            .fetchFirst() != null;

        if (!exists) {
            return Optional.empty();
        }

        return Optional.ofNullable(
            queryFactory
                .select(
                    new QInfluencerQueryResult_Detail(
                        QInfluencer.influencer.id,
                        QInfluencer.influencer.name,
                        QInfluencer.influencer.imgUrl,
                        QInfluencer.influencer.job,
                        userId == null ? //유저의 좋아요 여부
                            Expressions.constant(false) :
                            JPAExpressions
                                .selectOne()
                                .from(QLikedInfluencer.likedInfluencer)
                                .where(QLikedInfluencer.likedInfluencer.influencerId.eq(
                                        QInfluencer.influencer.id)
                                    .and(QLikedInfluencer.likedInfluencer.userId.eq(userId))
                                    .and(QLikedInfluencer.likedInfluencer.isLiked.isTrue()))
                                .exists(),
                        select(count(QLikedInfluencer.likedInfluencer.id)) // 팔로워 수
                            .from(QLikedInfluencer.likedInfluencer)
                            .where(QLikedInfluencer.likedInfluencer.influencerId.eq(
                                    QInfluencer.influencer.id)
                                .and(QLikedInfluencer.likedInfluencer.isLiked.isTrue())),
                        select(count(QVideo.video.id)) // 비디오 수
                            .from(QVideo.video)
                            .leftJoin(QPlaceVideo.placeVideo)
                            .on(QVideo.video.id.eq(QPlaceVideo.placeVideo.videoId))
                            .where(QVideo.video.influencerId.eq(QInfluencer.influencer.id)
                                .and(QPlaceVideo.placeVideo.isNotNull())
                                .and(QVideo.video.deleteAt.isNull()))
                    )
                )
                .from(QInfluencer.influencer)
                .where(QInfluencer.influencer.id.eq(influencerId))
                .fetchOne()
        );
    }

    @Override
    public List<String> getInfluencerNamesByPlaceId(Long placeId) {
        return queryFactory
            .select(QInfluencer.influencer.name)
            .from(QInfluencer.influencer)
            .leftJoin(QVideo.video).on(QInfluencer.influencer.id.eq(QVideo.video.influencerId))
            .leftJoin(QPlaceVideo.placeVideo).on(QVideo.video.id.eq(QPlaceVideo.placeVideo.videoId))
            .where(QPlaceVideo.placeVideo.placeId.eq(placeId)
                .and(QPlaceVideo.placeVideo.isNotNull())
                .and(QVideo.video.deleteAt.isNull())
                .and(QInfluencer.influencer.hidden.isFalse()))
            .fetch();
    }

    @Override
    public Page<Simple> getInfluencerSortedByLikes(Long userId, Pageable pageable) {
        var totalCount = queryFactory
            .select(count(QInfluencer.influencer.id))
            .from(QInfluencer.influencer)
            .where(QInfluencer.influencer.deleteAt.isNull()
                .and(QInfluencer.influencer.hidden.isFalse()))
            .fetchOne();

        if (totalCount == null || totalCount == 0) {
            return Page.empty(pageable);
        }

        var influencers = queryFactory
            .select(new QInfluencerQueryResult_Simple(
                QInfluencer.influencer.id,
                QInfluencer.influencer.name,
                QInfluencer.influencer.imgUrl,
                QInfluencer.influencer.job,
                QLikedInfluencer.likedInfluencer.isLiked.isTrue())
            )
            .from(QInfluencer.influencer)
            .leftJoin(QLikedInfluencer.likedInfluencer)
            .on(QLikedInfluencer.likedInfluencer.influencerId.eq(QInfluencer.influencer.id)
                .and(
                    userId == null ?
                        Expressions.FALSE :
                        QLikedInfluencer.likedInfluencer.userId.eq(userId)
                ))
            .where(QInfluencer.influencer.deleteAt.isNull()
                .and(QInfluencer.influencer.hidden.isFalse()))
            .orderBy(QLikedInfluencer.likedInfluencer.isLiked.desc(),
                QInfluencer.influencer.id.asc()
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(influencers, pageable, totalCount);
    }

}
