package my.inplace.infra.search;

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
        value = PlaceSearchQueryDslRepository.class
    ),
    scripts = "/sql/test-search-place.sql"
)
class PlaceSearchQueryDslRepositoryTest extends AbstractMySQLContainer {

    @Autowired
    PlaceSearchQueryDslRepository placeSearchRepository;

    @Test
    void searchAutoComplete() {
        // given
        String keyword = "장소1";
        List<SearchQueryResult.AutoComplete> expected = List.of(
            new SearchQueryResult.AutoComplete("테스트장소1", "place", 0.06741153448820114),
            new SearchQueryResult.AutoComplete("테스트장소10", "place", 0.06741153448820114),
            new SearchQueryResult.AutoComplete("테스트장소11", "place", 0.06741153448820114),
            new SearchQueryResult.AutoComplete("테스트장소12", "place", 0.06741153448820114),
            new SearchQueryResult.AutoComplete("테스트장소13", "place", 0.06741153448820114),
            new SearchQueryResult.AutoComplete("테스트장소14", "place", 0.06741153448820114),
            new SearchQueryResult.AutoComplete("테스트장소15", "place", 0.06741153448820114),
            new SearchQueryResult.AutoComplete("테스트장소16", "place", 0.06741153448820114),
            new SearchQueryResult.AutoComplete("테스트장소17", "place", 0.06741153448820114),
            new SearchQueryResult.AutoComplete("테스트장소18", "place", 0.06741153448820114)
        );

        // when
        List<SearchQueryResult.AutoComplete> actual = placeSearchRepository.searchAutoComplete(keyword);

        // then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    void search() {
        // given
        String keyword = "장소1";
        Pageable pageable = Pageable.ofSize(5);
        Long userId = 1L;
        List<SearchQueryResult.Place> expected = List.of(
            new SearchQueryResult.Place(1L, "테스트장소1", false),
            new SearchQueryResult.Place(10L, "테스트장소10", false),
            new SearchQueryResult.Place(11L, "테스트장소11", false),
            new SearchQueryResult.Place(12L, "테스트장소12", true),
            new SearchQueryResult.Place(13L, "테스트장소13", true)
        );

        // when
        Page<SearchQueryResult.Place> actual = placeSearchRepository.search(keyword, pageable, userId);
        // then

        assertThat(actual.getContent()).containsExactlyElementsOf(expected);
    }
}
