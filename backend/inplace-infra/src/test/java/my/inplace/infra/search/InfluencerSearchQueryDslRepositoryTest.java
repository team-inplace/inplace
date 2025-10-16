package my.inplace.infra.search;

import my.inplace.domain.influencer.query.InfluencerQueryResult;
import my.inplace.domain.search.SearchQueryResult;
import my.inplace.infra.config.AbstractMySQLContainer;
import my.inplace.infra.global.MySQLContainerJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MySQLContainerJpaTest(
    includeFilters = @Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = InfluencerSearchQueryDslRepository.class
    ),
    scripts = "/sql/test-search-influencer.sql"
)
class InfluencerSearchQueryDslRepositoryTest extends AbstractMySQLContainer {

    @Autowired
    private InfluencerSearchQueryDslRepository influencerSearchRepository;

    @Test
    void searchAutoComplete() {
        // given
        String keyword = "언서3";
        List<SearchQueryResult.AutoComplete> expected = List.of(
            new SearchQueryResult.AutoComplete("인플루언서3", "influencer", 0.3624762296676636)
        );

        // when
        List<SearchQueryResult.AutoComplete> actual = influencerSearchRepository.searchAutoComplete(keyword);

        // then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    void search() {
        // given
        String keyword = "인플루언서";
        Pageable pageable = Pageable.ofSize(5);
        Long userId = 1L;
        List<InfluencerQueryResult.Simple> expected = List.of(
            new InfluencerQueryResult.Simple(1L, "인플루언서1", "img1", "직업1", false),
            new InfluencerQueryResult.Simple(2L, "인플루언서2", "img2", "직업2", true),
            new InfluencerQueryResult.Simple(3L, "인플루언서3", "img3", "직업3", true),
            new InfluencerQueryResult.Simple(4L, "인플루언서4", "img4", "직업4", true)
        );

        // when
        Page<InfluencerQueryResult.Simple> actual = influencerSearchRepository.search(keyword, pageable, userId);

        // then
        assertThat(actual.getContent()).containsExactlyElementsOf(expected);
    }
}
