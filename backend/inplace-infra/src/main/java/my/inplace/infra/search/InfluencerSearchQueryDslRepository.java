package my.inplace.infra.search;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.influencer.QInfluencer;
import my.inplace.domain.influencer.QLikedInfluencer;
import my.inplace.domain.influencer.query.InfluencerQueryResult;
import my.inplace.domain.influencer.query.InfluencerQueryResult.Simple;
import my.inplace.domain.search.SearchQueryResult.AutoComplete;
import my.inplace.domain.search.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InfluencerSearchQueryDslRepository implements SearchRepository<Simple> {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AutoComplete> searchAutoComplete(String keyword) {
        var matchScore = getMatchScore(keyword);

        return queryFactory
            .select(
                QInfluencer.influencer.name,
                Expressions.constant("influencer"),
                matchScore.as("score")
            )
            .from(QInfluencer.influencer)
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
    public Page<InfluencerQueryResult.Simple> search(
        String keyword, Pageable pageable, Long userId) {
        var matchScore = getMatchScore(keyword);

        var content = queryFactory
            .select(Projections.constructor(InfluencerQueryResult.Simple.class,
                QInfluencer.influencer.id,
                QInfluencer.influencer.name,
                QInfluencer.influencer.imgUrl,
                QInfluencer.influencer.job,
                QLikedInfluencer.likedInfluencer.id.isNotNull()
            ))
            .from(QInfluencer.influencer)
            .leftJoin(QLikedInfluencer.likedInfluencer).on(
                QLikedInfluencer.likedInfluencer.influencerId.eq(QInfluencer.influencer.id),
                userId == null ? QLikedInfluencer.likedInfluencer.userId.isNull()
                    : QLikedInfluencer.likedInfluencer.userId.eq(userId)
            )
            .where(matchScore.gt(0))
            .orderBy(matchScore.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        var total = queryFactory
            .selectFrom(QInfluencer.influencer)
            .where(matchScore.gt(0))
            .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    private NumberTemplate<Double> getMatchScore(String keyword) {
        return Expressions.numberTemplate(
            Double.class,
            "function('match_against', {0}, {1})",
            QInfluencer.influencer.name,
            keyword
        );
    }
}
