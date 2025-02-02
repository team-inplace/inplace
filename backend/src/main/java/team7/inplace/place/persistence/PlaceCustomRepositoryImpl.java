package team7.inplace.place.persistence;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import team7.inplace.influencer.domain.QInfluencer;
import team7.inplace.place.domain.Category;
import team7.inplace.place.domain.Place;
import team7.inplace.place.domain.QPlace;
import team7.inplace.video.domain.QVideo;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class PlaceCustomRepositoryImpl implements PlaceCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Place> findPlacesByDistance(String longitude, String latitude, Pageable pageable) {
        QPlace place = QPlace.place;

        NumberTemplate<Double> distanceExpression = Expressions.numberTemplate(Double.class,
                "6371 * acos(cos(radians({0})) * cos(radians(CAST({1} AS DOUBLE))) * cos(radians(CAST({2} AS DOUBLE)) - radians({3})) + sin(radians({0})) * sin(radians(CAST({1} AS DOUBLE))))",
                Double.parseDouble(latitude), place.coordinate.latitude, place.coordinate.longitude,
                Double.parseDouble(longitude));

        List<Place> places = jpaQueryFactory.selectFrom(place)
                .orderBy(distanceExpression.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(place.id.count()) // 중복 제거
            .from(place);

        return PageableExecutionUtils.getPage(places, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Place> findPlacesByDistanceAndFilters(String topLeftLongitude,
                                                      String topLeftLatitude, String bottomRightLongitude, String bottomRightLatitude,
                                                      String longitude, String latitude, List<String> categories, List<String> influencers,
                                                      Pageable pageable) {

        QPlace place = QPlace.place;
        QVideo video = QVideo.video;
        QInfluencer influencer = QInfluencer.influencer;

        NumberTemplate<Double> distanceExpression = Expressions.numberTemplate(Double.class,
                "6371 * acos(cos(radians({0})) * cos(radians(CAST({1} AS DOUBLE))) * cos(radians(CAST({2} AS DOUBLE)) - radians({3})) + sin(radians({0})) * sin(radians(CAST({1} AS DOUBLE))))",
                Double.parseDouble(latitude), place.coordinate.latitude, place.coordinate.longitude,
                Double.parseDouble(longitude));

        // 1. content를 가져오는 fetch() 쿼리
        List<Place> content = jpaQueryFactory.selectFrom(place)
                .leftJoin(video).on(video.place.eq(place))
                .leftJoin(influencer).on(video.influencer.eq(influencer))
                .where(
                        withinBoundary(
                                place,
                                Double.parseDouble(topLeftLongitude),
                                Double.parseDouble(topLeftLatitude),
                                Double.parseDouble(bottomRightLongitude),
                                Double.parseDouble(bottomRightLatitude)
                        ),
                        placeCategoryIn(categories),
                        placeInfluencerIn(influencers)
                )
                .orderBy(distanceExpression.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2. countQuery를 따로 선언하여 필요할 때만 실행
        JPAQuery<Long> countQuery = jpaQueryFactory.select(place.id.count()) // 중복 제거
                .from(place)
                .leftJoin(video).on(video.place.eq(place))
                .leftJoin(influencer).on(video.influencer.eq(influencer))
                .where(
                        withinBoundary(
                                place,
                                Double.parseDouble(topLeftLongitude),
                                Double.parseDouble(topLeftLatitude),
                                Double.parseDouble(bottomRightLongitude),
                                Double.parseDouble(bottomRightLatitude)
                        ),
                        placeCategoryIn(categories),
                        placeInfluencerIn(influencers)
                );

        // 3. PageableExecutionUtils를 사용하여 필요할 때 countQuery 실행
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression withinBoundary(QPlace place, double topLeftLongitude,
                                             double topLeftLatitude,
                                             double bottomRightLongitude, double bottomRightLatitude) {
        NumberTemplate<Double> longitude = Expressions.numberTemplate(Double.class,
                "CAST({0} AS DOUBLE)", place.coordinate.longitude);
        NumberTemplate<Double> latitude = Expressions.numberTemplate(Double.class,
                "CAST({0} AS DOUBLE)", place.coordinate.latitude);

        return longitude.between(topLeftLongitude, bottomRightLongitude)
                .and(latitude.between(bottomRightLatitude, topLeftLatitude));
    }

    private BooleanExpression placeCategoryIn(List<String> categories) {
        if (categories == null) {
            return null;
        }

        List<Category> enumCategories = categories.stream()
                .map(Category::of) // Category.of() 메서드로 직접 매핑
                .collect(Collectors.toList());

        return QPlace.place.category.in(enumCategories);
    }

    private BooleanExpression placeInfluencerIn(List<String> influencers) {
        if (influencers == null) {
            return null;
        }

        return QInfluencer.influencer.name.in(influencers);
    }
}
