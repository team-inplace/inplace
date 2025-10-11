package my.inplace.infra.search;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.inplace.domain.influencer.QInfluencer;
import my.inplace.domain.place.QCategory;
import my.inplace.domain.place.QPlace;
import my.inplace.domain.place.QPlaceVideo;
import my.inplace.domain.search.SearchRepository;
import my.inplace.domain.video.QVideo;
import my.inplace.domain.video.query.VideoQueryResult;
import my.inplace.domain.video.query.VideoQueryResult.DetailedVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class VideoSearchQueryDslRepository implements SearchRepository<DetailedVideo> {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<VideoQueryResult.DetailedVideo> search(
        String keyword, Pageable pageable, Long userId) {

        var count = queryFactory
            .select(QVideo.video.count())
            .from(QVideo.video)
            .innerJoin(QPlaceVideo.placeVideo)
            .on(QVideo.video.id.eq(QPlaceVideo.placeVideo.videoId))
            .innerJoin(QPlace.place).on(QPlaceVideo.placeVideo.placeId.eq(QPlace.place.id))
            .innerJoin(QInfluencer.influencer)
            .on(QVideo.video.influencerId.eq(QInfluencer.influencer.id))
            .where(getPlaceMatchScore(keyword).gt(0)
                .or(getInfluencerMatchScore(keyword).gt(0)
                    .and(QPlace.place.id.isNotNull())
                    .and(QInfluencer.influencer.id.isNotNull())
                    .and(QPlaceVideo.placeVideo.isNotNull()))
            ).fetchOne();

        if (count == null || count == 0) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        QCategory selfCategory = new QCategory("selfCategory");
        var contents = queryFactory
            .select(Projections.constructor(VideoQueryResult.DetailedVideo.class,
                QVideo.video.id,
                QVideo.video.uuid,
                QInfluencer.influencer.name,
                QPlace.place.id,
                QPlace.place.name,
                selfCategory.engName, // 상위 카테고리 영어 이름
                QCategory.category.parentId,
                QPlace.place.address.address1,
                QPlace.place.address.address2,
                QPlace.place.address.address3
            ))
            .from(QVideo.video)
            .innerJoin(QInfluencer.influencer)
            .on(QVideo.video.influencerId.eq(QInfluencer.influencer.id))
            .innerJoin(QPlaceVideo.placeVideo)
            .on(QVideo.video.id.eq(QPlaceVideo.placeVideo.videoId))
            .innerJoin(QPlace.place).on(QPlaceVideo.placeVideo.placeId.eq(QPlace.place.id))
            .innerJoin(QCategory.category).on(QPlace.place.categoryId.eq(QCategory.category.id))
            .leftJoin(selfCategory).on(QCategory.category.parentId.eq(selfCategory.id))
            .where(getPlaceMatchScore(keyword).gt(0)
                .or(getInfluencerMatchScore(keyword).gt(0))
                .and(QPlace.place.id.isNotNull())
                .and(QPlaceVideo.placeVideo.isNotNull())
                .and(QInfluencer.influencer.id.isNotNull()))
            .orderBy(getPlaceMatchScore(keyword).desc(), getInfluencerMatchScore(keyword).desc())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        return new PageImpl<>(contents, pageable, count);
    }

    private NumberTemplate<Double> getPlaceMatchScore(String keyword) {
        return Expressions.numberTemplate(
            Double.class,
            "function('match_against', {0}, {1})",
            QPlace.place.name,
            keyword
        );
    }


    private NumberTemplate<Double> getInfluencerMatchScore(String keyword) {
        return Expressions.numberTemplate(
            Double.class,
            "function('match_against', {0}, {1})",
            QInfluencer.influencer.name,
            keyword
        );
    }
}
