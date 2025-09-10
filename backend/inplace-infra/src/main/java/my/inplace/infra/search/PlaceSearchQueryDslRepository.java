package my.inplace.infra.search;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.place.QLikedPlace;
import my.inplace.domain.place.QPlace;
import my.inplace.domain.search.SearchQueryResult;
import my.inplace.domain.search.SearchQueryResult.AutoComplete;
import my.inplace.domain.search.SearchQueryResult.Place;
import my.inplace.domain.search.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlaceSearchQueryDslRepository implements SearchRepository<Place> {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AutoComplete> searchAutoComplete(String keyword) {
        var matchScore = getMatchScore(keyword);

        return queryFactory
            .select(
                QPlace.place.name,
                Expressions.constant("place"),
                matchScore.as("score")
            )
            .from(QPlace.place)
            .where(matchScore.gt(0))
            .orderBy(matchScore.desc())
            .limit(SEARCH_LIMIT)
            .fetch()
            .stream()
            .map(tuple -> new AutoComplete(
                tuple.get(0, String.class),
                tuple.get(1, String.class),
                tuple.get(2, Double.class)
            )).toList();
    }

    @Override
    public Page<SearchQueryResult.Place> search(String keyword, Pageable pageable, Long userId) {
        var matchScore = getMatchScore(keyword);

        var content = queryFactory
            .select(
                Projections.constructor(SearchQueryResult.Place.class,
                    QPlace.place.id,
                    QPlace.place.name,
                    QLikedPlace.likedPlace.id.isNotNull())
            )
            .from(QPlace.place)
            .leftJoin(QLikedPlace.likedPlace).on(
                QLikedPlace.likedPlace.placeId.eq(QPlace.place.id),
                userId == null ? QLikedPlace.likedPlace.userId.isNull()
                    : QLikedPlace.likedPlace.userId.eq(userId)
            )
            .where(matchScore.gt(0))
            .orderBy(matchScore.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        var total = queryFactory
            .selectFrom(QPlace.place)
            .where(matchScore.gt(0))
            .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    private NumberTemplate<Double> getMatchScore(String keyword) {
        return Expressions.numberTemplate(
            Double.class,
            "function('match_against', {0}, {1})",
            QPlace.place.name,
            keyword
        );
    }
}
