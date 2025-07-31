package search;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import search.SearchQueryResult.AutoComplete;
import search.SearchQueryResult.Place;
import team7.inplace.liked.likedPlace.domain.QLikedPlace;
import team7.inplace.place.domain.QPlace;
import team7.inplace.search.persistence.dto.QSearchQueryResult_Place;

@Repository
@RequiredArgsConstructor
public class PlaceSearchRepository implements SearchRepository<Place> {

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
            .select(new QSearchQueryResult_Place(
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
