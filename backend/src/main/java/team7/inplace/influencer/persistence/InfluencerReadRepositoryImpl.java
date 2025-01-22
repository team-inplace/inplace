package team7.inplace.influencer.persistence;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team7.inplace.influencer.domain.QInfluencer;
import team7.inplace.influencer.persistence.dto.InfluencerQueryResult;
import team7.inplace.influencer.persistence.dto.QInfluencerQueryResult_Detail;
import team7.inplace.liked.likedInfluencer.domain.QLikedInfluencer;
import team7.inplace.video.domain.QVideo;

@Repository
@RequiredArgsConstructor
public class InfluencerReadRepositoryImpl implements InfluencerReadRepository {

    private final JPAQueryFactory queryFactory;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<InfluencerQueryResult.Detail> getInfluencerDetail(
        Long influencerId,
        Long userId
    ) {
        return Optional.ofNullable(
            queryFactory
                .select(
                    new QInfluencerQueryResult_Detail(
                        QInfluencer.influencer.id,
                        QInfluencer.influencer.name,
                        QInfluencer.influencer.imgUrl,
                        QInfluencer.influencer.job,
                        userId == null ?
                            Expressions.constant(false) :
                            QLikedInfluencer.likedInfluencer.id.in(userId).isNotNull(),
                        QLikedInfluencer.likedInfluencer.id.count(),
                        QVideo.video.id.count()
                    )
                ).distinct()
                .from(QInfluencer.influencer)
                .leftJoin(QLikedInfluencer.likedInfluencer)
                .on(QInfluencer.influencer.id.eq(QLikedInfluencer.likedInfluencer.influencerId)
                    .and(QLikedInfluencer.likedInfluencer.isLiked.isTrue()))
                .join(QVideo.video).on(QInfluencer.influencer.id.eq(QVideo.video.influencerId))
                .where(QInfluencer.influencer.id.eq(influencerId),
                    QInfluencer.influencer.deleteAt.isNull(),
                    QVideo.video.deleteAt.isNull(),
                    QLikedInfluencer.likedInfluencer.deleteAt.isNull())
                .fetchOne()
        );
    }

    @Override
    public List<String> getInfluencerNamesByPlaceId(Long placeId) {
        return jpaQueryFactory
            .select(QInfluencer.influencer.name)
            .from(QInfluencer.influencer)
            .leftJoin(QVideo.video).on(QInfluencer.influencer.id.eq(QVideo.video.influencerId))
            .where(QVideo.video.placeId.eq(placeId))
            .fetch();
    }

}
