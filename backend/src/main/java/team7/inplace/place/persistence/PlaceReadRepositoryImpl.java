package team7.inplace.place.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import team7.inplace.influencer.domain.QInfluencer;
import team7.inplace.liked.likedPlace.domain.QLikedPlace;
import team7.inplace.place.application.command.PlacesCommand.RegionParam;
import team7.inplace.place.domain.QCategory;
import team7.inplace.place.domain.QPlace;
import team7.inplace.place.domain.QPlaceVideo;
import team7.inplace.place.persistence.dto.PlaceQueryResult.DetailedPlace;
import team7.inplace.place.persistence.dto.PlaceQueryResult.Marker;
import team7.inplace.place.persistence.dto.PlaceQueryResult.MarkerDetail;
import team7.inplace.place.persistence.dto.PlaceQueryResult.SimplePlace;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_DetailedPlace;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_Marker;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_MarkerDetail;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_SimplePlace;
import team7.inplace.video.domain.QVideo;

@Repository
@RequiredArgsConstructor
public class PlaceReadRepositoryImpl implements PlaceReadRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<DetailedPlace> findDetailedPlaceById(Long userId, Long placeId) {
        DetailedPlace place = buildDetailedPlaceQuery(userId)
            .where(QPlace.place.id.eq(placeId), QPlace.place.deleteAt.isNull())
            .groupBy(QPlace.place.id)
            .fetchOne();
        return Optional.ofNullable(place);
    }

    @Override
    public Page<DetailedPlace> findPlacesInMapRangeWithPaging(
        Double topLeftLongitude, Double topLeftLatitude,
        Double bottomRightLongitude, Double bottomRightLatitude,
        Double longitude, Double latitude,
        List<RegionParam> regionFilters, List<Long> categoryFilters,
        List<String> influencerFilters, Pageable pageable, Long userId
    ) {
        List<Long> placeIds = getFilteredPlaceIds(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude,
            regionFilters, categoryFilters, influencerFilters
        );

        if (placeIds.isEmpty()) {
            return Page.empty(pageable);
        }

        List<DetailedPlace> results = buildDetailedPlaceQuery(userId)
            .where(QPlace.place.id.in(placeIds))
            .groupBy(QPlace.place.id)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return PageableExecutionUtils.getPage(results, pageable, () -> (long) placeIds.size());
    }

    @Override
    public List<Marker> findPlaceLocationsInMapRange(
        Double topLeftLongitude, Double topLeftLatitude,
        Double bottomRightLongitude, Double bottomRightLatitude,
        List<RegionParam> regionParams, List<Long> categories, List<String> influencers
    ) {
        List<Long> ids = getFilteredPlaceIds(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude,
            regionParams, categories, influencers
        );
        return buildMarkerQuery().where(QPlace.place.id.in(ids)).fetch();
    }

    @Override
    public Optional<SimplePlace> findSimplePlaceById(Long placeId) {
        SimplePlace place = jpaQueryFactory
            .select(new QPlaceQueryResult_SimplePlace(
                QPlace.place.id, QPlace.place.name,
                QPlace.place.address.address1,
                QPlace.place.address.address2,
                QPlace.place.address.address3
            ))
            .from(QPlace.place)
            .where(QPlace.place.id.eq(placeId), QPlace.place.deleteAt.isNull())
            .fetchOne();
        return Optional.ofNullable(place);
    }

    @Override
    public Page<DetailedPlace> findLikedPlacesByUserIdWithPaging(Long userId, Pageable pageable) {
        List<Long> placeIds = jpaQueryFactory
            .select(QLikedPlace.likedPlace.placeId)
            .from(QLikedPlace.likedPlace)
            .where(QLikedPlace.likedPlace.userId.eq(userId),
                QLikedPlace.likedPlace.isLiked.isTrue(),
                QLikedPlace.likedPlace.deleteAt.isNull())
            .distinct()
            .fetch();

        if (placeIds.isEmpty()) {
            return Page.empty(pageable);
        }

        List<DetailedPlace> results = buildDetailedPlaceQuery(userId)
            .where(QPlace.place.id.in(placeIds))
            .groupBy(QPlace.place.id)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return PageableExecutionUtils.getPage(results, pageable, () -> (long) placeIds.size());
    }

    @Override
    public MarkerDetail findPlaceMarkerById(Long placeId) {
        return jpaQueryFactory
            .select(new QPlaceQueryResult_MarkerDetail(
                QPlace.place.id, QPlace.place.name, QCategory.category.name,
                QPlace.place.address.address1, QPlace.place.address.address2,
                QPlace.place.address.address3
            ))
            .from(QPlace.place)
            .leftJoin(QCategory.category).on(QPlace.place.categoryId.eq(QCategory.category.id))
            .where(QPlace.place.id.eq(placeId))
            .fetchOne();
    }

    @Override
    public List<Marker> findPlaceLocationsByName(
        String name, List<RegionParam> regionParams, List<Long> categories,
        List<String> influencers
    ) {
        List<Long> ids = getFilteredPlaceIdsByName(name, regionParams, categories, influencers);
        return buildMarkerQuery().where(QPlace.place.id.in(ids)).fetch();
    }

    @Override
    public Page<DetailedPlace> findPlacesByNameWithPaging(
        Long userId, String name,
        List<RegionParam> regionParams, List<Long> categories,
        List<String> influencers, Pageable pageable
    ) {
        List<Long> ids = getFilteredPlaceIdsByName(name, regionParams, categories, influencers);

        if (ids.isEmpty()) {
            return Page.empty(pageable);
        }

        List<DetailedPlace> places = buildDetailedPlaceQuery(userId)
            .where(QPlace.place.id.in(ids))
            .groupBy(QPlace.place.id)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return PageableExecutionUtils.getPage(places, pageable, () -> (long) ids.size());
    }

    @Override
    public List<DetailedPlace> getDetailedPlacesByVideoId(Long videoId) {
        List<Long> placeIds = jpaQueryFactory
            .select(QPlaceVideo.placeVideo.placeId)
            .from(QVideo.video)
            .leftJoin(QPlaceVideo.placeVideo).on(QVideo.video.id.eq(QPlaceVideo.placeVideo.videoId))
            .where(QVideo.video.id.eq(videoId))
            .where(QPlace.place.deleteAt.isNull())
            .fetch();

        return jpaQueryFactory
            .select(new QPlaceQueryResult_DetailedPlace(
                QPlace.place.id,
                QPlace.place.name,
                QPlace.place.address.address1,
                QPlace.place.address.address2,
                QPlace.place.address.address3,
                QPlace.place.coordinate.longitude,
                QPlace.place.coordinate.latitude,
                QCategory.category.name,
                QPlace.place.googlePlaceId,
                QPlace.place.kakaoPlaceId,
                Expressions.nullExpression(),
                Expressions.nullExpression()
            ))
            .from(QPlace.place)
            .where(QPlace.place.id.in(placeIds))
            .leftJoin(QCategory.category).on(QPlace.place.categoryId.eq(QCategory.category.id))
            .fetch();
    }

    // ====================== 공통 빌더 =========================

    private JPAQuery<DetailedPlace> buildDetailedPlaceQuery(Long userId) {
        QLikedPlace liked = new QLikedPlace("liked");
        QLikedPlace selfLiked = new QLikedPlace("selfLiked");

        return jpaQueryFactory
            .select(new QPlaceQueryResult_DetailedPlace(
                QPlace.place.id,
                QPlace.place.name,
                QPlace.place.address.address1,
                QPlace.place.address.address2,
                QPlace.place.address.address3,
                QPlace.place.coordinate.longitude,
                QPlace.place.coordinate.latitude,
                QCategory.category.name,
                QPlace.place.googlePlaceId,
                QPlace.place.kakaoPlaceId,
                liked.id.countDistinct(),
                Expressions.numberTemplate(Integer.class, "MAX(CASE WHEN {0} THEN 1 ELSE 0 END)",
                    selfLiked.id.isNotNull()).eq(1)
            ))
            .from(QPlace.place)
            .leftJoin(QCategory.category).on(QPlace.place.categoryId.eq(QCategory.category.id))
            .leftJoin(liked).on(liked.placeId.eq(QPlace.place.id).and(liked.isLiked.isTrue()))
            .leftJoin(selfLiked).on(selfLiked.placeId.eq(QPlace.place.id)
                .and(userId != null ? selfLiked.userId.eq(userId) : selfLiked.userId.isNull())
                .and(selfLiked.isLiked.isTrue()));
    }

    private JPAQuery<Marker> buildMarkerQuery() {
        return jpaQueryFactory
            .select(new QPlaceQueryResult_Marker(
                QPlace.place.id,
                QPlace.place.coordinate.longitude,
                QPlace.place.coordinate.latitude
            ))
            .from(QPlace.place);
    }

    // ====================== 필터 처리 =========================

    private List<Long> getFilteredPlaceIds(
        Double topLeftLng, Double topLeftLat,
        Double bottomRightLng, Double bottomRightLat,
        List<RegionParam> regions, List<Long> categories, List<String> influencers
    ) {
        return jpaQueryFactory
            .select(QPlace.place.id)
            .from(QPlace.place)
            .innerJoin(QCategory.category).on(QPlace.place.categoryId.eq(QCategory.category.id))
            .innerJoin(QPlaceVideo.placeVideo)
            .on(QPlaceVideo.placeVideo.placeId.eq(QPlace.place.id))
            .innerJoin(QVideo.video).on(QPlaceVideo.placeVideo.videoId.eq(QVideo.video.id))
            .innerJoin(QInfluencer.influencer)
            .on(QVideo.video.influencerId.eq(QInfluencer.influencer.id))
            .where(
                buildLocationCondition(regions, topLeftLng, topLeftLat, bottomRightLng,
                    bottomRightLat),
                buildFilterCondition(categories, influencers),
                commonWhere()
            )
            .distinct()
            .fetch();
    }

    private List<Long> getFilteredPlaceIdsByName(
        String name, List<RegionParam> regions, List<Long> categories, List<String> influencers
    ) {
        return jpaQueryFactory
            .select(QPlace.place.id)
            .from(QPlace.place)
            .innerJoin(QCategory.category).on(QPlace.place.categoryId.eq(QCategory.category.id))
            .innerJoin(QPlaceVideo.placeVideo)
            .on(QPlaceVideo.placeVideo.placeId.eq(QPlace.place.id))
            .innerJoin(QVideo.video).on(QPlaceVideo.placeVideo.videoId.eq(QVideo.video.id))
            .innerJoin(QInfluencer.influencer)
            .on(QVideo.video.influencerId.eq(QInfluencer.influencer.id))
            .where(
                buildLocationCondition(regions, null, null, null, null),
                buildFilterCondition(categories, influencers),
                getMatchScore(name).gt(0),
                commonWhere()
            )
            .distinct()
            .fetch();
    }

    // ====================== 조건 처리 =========================

    private BooleanExpression commonWhere() {
        return QPlace.place.deleteAt.isNull()
            .and(QVideo.video.deleteAt.isNull())
            .and(QInfluencer.influencer.deleteAt.isNull());
    }

    private BooleanBuilder buildFilterCondition(
        List<Long> categories,
        List<String> influencers
    ) {
        BooleanBuilder builder = new BooleanBuilder();
        if (categories != null && !categories.isEmpty()) {
            builder.and(QCategory.category.id.in(categories))
                .or(QCategory.category.parentId.in(categories));
        }
        if (influencers != null && !influencers.isEmpty()) {
            builder.and(QInfluencer.influencer.name.in(influencers));
        }
        return builder;
    }

    private BooleanBuilder buildLocationCondition(
        List<RegionParam> regions,
        Double topLeftLng, Double topLeftLat,
        Double bottomRightLng, Double bottomRightLat
    ) {
        BooleanBuilder builder = new BooleanBuilder();
        if (regions != null && !regions.isEmpty()) {
            for (RegionParam region : regions) {
                BooleanExpression city = QPlace.place.address.address1.eq(region.city());
                BooleanExpression district = region.district() == null
                    ? Expressions.TRUE
                    : QPlace.place.address.address2.eq(region.district());
                builder.or(city.and(district));
            }
        } else if (topLeftLng != null && bottomRightLng != null &&
            topLeftLat != null && bottomRightLat != null) {
            builder.and(QPlace.place.coordinate.longitude.between(topLeftLng, bottomRightLng))
                .and(QPlace.place.coordinate.latitude.between(bottomRightLat, topLeftLat));
        }
        return builder;
    }

    private NumberTemplate<Double> getMatchScore(String keyword) {
        return Expressions.numberTemplate(Double.class,
            "function('match_against', {0}, {1})",
            QPlace.place.name, keyword);
    }
}
