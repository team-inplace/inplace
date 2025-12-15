package my.inplace.infra.video;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.spatial.locationtech.jts.JTSGeometryExpressions;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.influencer.QInfluencer;
import my.inplace.domain.influencer.QLikedInfluencer;
import my.inplace.domain.place.QCategory;
import my.inplace.domain.place.QLikedPlace;
import my.inplace.domain.place.QPlace;
import my.inplace.domain.place.QPlaceVideo;
import my.inplace.domain.util.SingletonGeometryFactory;
import my.inplace.domain.video.QVideo;
import my.inplace.domain.video.query.VideoQueryParam;
import my.inplace.domain.video.query.VideoQueryResult;
import my.inplace.domain.video.query.VideoQueryResult.AdminVideo;
import my.inplace.domain.video.query.VideoQueryResult.DetailedVideo;
import my.inplace.domain.video.query.VideoQueryResult.SimpleVideo;
import my.inplace.domain.video.query.VideoReadRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VideoReadQueryDslRepository implements VideoReadRepository {

    private static final int RANGE_IN_METERS = 3000;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<DetailedVideo> findSimpleVideosInSurround(
        VideoQueryParam.SquareBound squareBound,
        Pageable pageable
    ) {
        var longitude = squareBound.longitude();
        var latitude = squareBound.latitude();

        Long total = queryFactory
            .select(QVideo.video.count())
            .from(QVideo.video)
            .join(QPlaceVideo.placeVideo).on(QVideo.video.id.eq(QPlaceVideo.placeVideo.videoId))
            .join(QPlace.place).on(QPlaceVideo.placeVideo.placeId.eq(QPlace.place.id))
            .where(locationCondition(longitude, latitude),
                QVideo.video.deleteAt.isNull(),
                QPlace.place.deleteAt.isNull())
            .fetchOne();

        if (total == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, total);
        }

        List<DetailedVideo> content = buildDetailedVideoQuery()
            .where(locationCondition(longitude, latitude),
                QVideo.video.deleteAt.isNull(),
                QPlace.place.deleteAt.isNull(),
                QInfluencer.influencer.deleteAt.isNull())
            .orderBy(QVideo.video.publishTime.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<DetailedVideo> findTop10ByViewCountIncrement(Long parentCategoryId) {
        return buildDetailedVideoQuery()
            .where(commonWhere().and(QCategory.category.parentId.eq(parentCategoryId)))
            .orderBy(QVideo.video.view.viewCountIncrease.desc())
            .limit(10)
            .fetch();
    }

    @Override
    public List<DetailedVideo> findTop10ByLatestUploadDate() {
        return buildDetailedVideoQuery()
            .where(commonWhere())
            .orderBy(QVideo.video.publishTime.desc())
            .limit(10)
            .fetch();
    }

    @Override
    public List<DetailedVideo> findTop10ByLikedInfluencer(Long userId) {
        return buildDetailedVideoQuery()
            .where(
                QVideo.video.influencerId.in(
                    JPAExpressions.select(QLikedInfluencer.likedInfluencer.influencerId)
                        .from(QLikedInfluencer.likedInfluencer)
                        .where(QLikedInfluencer.likedInfluencer.userId.eq(userId)
                            .and(QLikedInfluencer.likedInfluencer.isLiked.isTrue()))),
                commonWhere())
            .orderBy(QVideo.video.publishTime.desc(), QPlace.place.id.asc())
            .limit(10)
            .fetch();
    }

    @Override
    public List<SimpleVideo> findSimpleVideosByPlaceId(Long placeId) {
        return buildSimpleVideoQuery()
            .where(QPlaceVideo.placeVideo.placeId.eq(placeId),
                commonWhere().and(QPlaceVideo.placeVideo.isNotNull()))
            .fetch();
    }

    @Override
    public Map<Long, List<SimpleVideo>> findSimpleVideosByPlaceIds(List<Long> placeIds) {
        if (placeIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return buildSimpleVideoQuery()
            .where(QPlaceVideo.placeVideo.placeId.in(placeIds),
                commonWhere().and(QPlaceVideo.placeVideo.isNotNull()))
            .fetch()
            .stream()
            .collect(Collectors.groupingBy(SimpleVideo::placeId));
    }

    @Override
    public Page<SimpleVideo> findVideoWithNoPlace(Pageable pageable) {
        List<Long> videoIds = queryFactory
            .select(QVideo.video.id)
            .from(QVideo.video)
            .where(
                JPAExpressions
                    .selectOne()
                    .from(QPlaceVideo.placeVideo)
                    .where(QPlaceVideo.placeVideo.videoId.eq(QVideo.video.id))
                    .notExists(),
                QVideo.video.deleteAt.isNull()
            )
            .fetch();

        if (videoIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        List<SimpleVideo> videos = queryFactory
            .select(Projections.constructor(VideoQueryResult.SimpleVideo.class,
                QVideo.video.id,
                QVideo.video.uuid,
                QInfluencer.influencer.name,
                QPlace.place.id,
                QPlace.place.name,
                QCategory.category.name))
            .from(QVideo.video)
            .leftJoin(QPlaceVideo.placeVideo)
            .on(QVideo.video.id.eq(QPlaceVideo.placeVideo.videoId))
            .leftJoin(QPlace.place).on(QPlaceVideo.placeVideo.placeId.eq(QPlace.place.id))
            .leftJoin(QCategory.category).on(QPlace.place.categoryId.eq(QCategory.category.id))
            .leftJoin(QInfluencer.influencer)
            .on(QVideo.video.influencerId.eq(QInfluencer.influencer.id))
            .where(QVideo.video.id.in(videoIds),
                commonWhere())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(videos, pageable, videoIds.size());
    }

    @Override
    public Page<DetailedVideo> findDetailedVideosWithOneInfluencerId(
        Long influencerId, Pageable pageable
    ) {
        Long total = queryFactory
            .select(QVideo.video.countDistinct())
            .from(QVideo.video)
            .innerJoin(QPlaceVideo.placeVideo)
            .on(QVideo.video.id.eq(QPlaceVideo.placeVideo.videoId))
            .where(QVideo.video.influencerId.eq(influencerId),
                QVideo.video.deleteAt.isNull())
            .fetchOne();

        if (total == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, total);
        }

        var query = buildDetailedVideoQuery()
            .where(QVideo.video.influencerId.eq(influencerId), commonWhere());

        applySorting(query, pageable);

        List<DetailedVideo> videos = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(videos, pageable, total);
    }

    @Override
    public Page<AdminVideo> findAdminVideoByCondition(
        VideoQueryParam.Condition condition, Pageable pageable) {
        Long total = queryFactory
            .select(QVideo.video.countDistinct())
            .from(QVideo.video)
            .leftJoin(QPlaceVideo.placeVideo).on(QVideo.video.id.eq(QPlaceVideo.placeVideo.videoId))
            .where(
                eqInfluencerId(condition.influencerId()),
                registrationCondition(condition.placeRegistration()),
                QVideo.video.deleteAt.isNull()
            )
            .fetchOne();

        if (total == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, total);
        }

        BooleanBuilder where = new BooleanBuilder().and(QVideo.video.deleteAt.isNull());

        if (condition.influencerId() != null) {
            where.and(QVideo.video.influencerId.eq(condition.influencerId()));
        }

        if (condition.placeRegistration()) {
            where.and(
                JPAExpressions.selectOne()
                    .from(QPlaceVideo.placeVideo)
                    .where(QPlaceVideo.placeVideo.videoId.eq(QVideo.video.id))
                    .exists()
            );
        }
        else {
            where.and(
                JPAExpressions.selectOne()
                    .from(QPlaceVideo.placeVideo)
                    .where(QPlaceVideo.placeVideo.videoId.eq(QVideo.video.id))
                    .notExists()
            );
        }

        List<AdminVideo> videos = queryFactory
            .select(Projections.constructor(VideoQueryResult.AdminVideo.class,
                QVideo.video.id,
                QVideo.video.uuid,
                Expressions.constant(Boolean.TRUE.equals(condition.placeRegistration()))
            ))
            .from(QVideo.video)
            .where(where)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(videos, pageable, total);
    }

    private BooleanExpression registrationCondition(Boolean registered) {
        if (Objects.isNull(registered) || !registered) {
            return QPlaceVideo.placeVideo.isNull();
        }
        return QPlaceVideo.placeVideo.isNotNull();
    }

    private BooleanExpression eqInfluencerId(Long influencerId) {
        return influencerId == null ? null : QVideo.video.influencerId.eq(influencerId);
    }

    private JPAQuery<SimpleVideo> buildSimpleVideoQuery() {
        return queryFactory
            .select(Projections.constructor(VideoQueryResult.SimpleVideo.class,
                QVideo.video.id,
                QVideo.video.uuid,
                QInfluencer.influencer.name,
                QPlace.place.id,
                QPlace.place.name,
                QCategory.category.name))
            .from(QVideo.video)
            .innerJoin(QPlaceVideo.placeVideo)
            .on(QVideo.video.id.eq(QPlaceVideo.placeVideo.videoId))
            .innerJoin(QPlace.place).on(QPlaceVideo.placeVideo.placeId.eq(QPlace.place.id))
            .innerJoin(QCategory.category).on(QPlace.place.categoryId.eq(QCategory.category.id))
            .innerJoin(QInfluencer.influencer)
            .on(QVideo.video.influencerId.eq(QInfluencer.influencer.id));
    }

    private JPAQuery<DetailedVideo> buildDetailedVideoQuery() {
        QCategory selfCategory = new QCategory("selfCategory");

        return queryFactory
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
            .leftJoin(selfCategory).on(QCategory.category.parentId.eq(selfCategory.id));
    }

    private BooleanExpression locationCondition(double longitude, double latitude) {
        return Expressions.numberTemplate(
            Double.class,
            "ST_Distance_Sphere({0}, {1})",
            QPlace.place.location,
            Expressions.constant(SingletonGeometryFactory.newPoint(longitude, latitude))
        ).loe(RANGE_IN_METERS);
    }

    private BooleanExpression commonWhere() {
        return QVideo.video.deleteAt.isNull()
            .and(QPlace.place.deleteAt.isNull())
            .and(QInfluencer.influencer.deleteAt.isNull());
    }

    private void applySorting(JPAQuery<DetailedVideo> query, Pageable pageable) {
        pageable.getSort().stream()
            .findFirst()
            .ifPresentOrElse(order -> {
                    String property = order.getProperty();
                    OrderSpecifier<?>[] specifier = switch (property) {
                        case "popularity" -> new OrderSpecifier<?>[]{
                            QVideo.video.view.viewCountIncrease.desc(),
                            QVideo.video.publishTime.desc()
                        };
                        case "likes" -> {
                            var likesCount = JPAExpressions
                                .select(QLikedPlace.likedPlace.count())
                                .from(QLikedPlace.likedPlace)
                                .where(QLikedPlace.likedPlace.placeId.eq(QPlace.place.id)
                                    .and(QLikedPlace.likedPlace.isLiked.isTrue()));
                            yield new OrderSpecifier<?>[]{
                                Expressions.numberTemplate(Long.class, "COALESCE({0}, 0)",
                                    likesCount).desc(),
                                QVideo.video.publishTime.desc()
                            };
                        }
                        default -> new OrderSpecifier<?>[]{QVideo.video.publishTime.desc()};
                    };
                    query.orderBy(specifier);
                }, () -> query.orderBy(QVideo.video.publishTime.desc())
            );
    }
}
