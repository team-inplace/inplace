package my.inplace.infra.influencer;

import my.inplace.domain.influencer.query.InfluencerQueryResult;
import my.inplace.infra.config.AbstractMySQLContainer;
import my.inplace.infra.global.MySQLContainerJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MySQLContainerJpaTest(
    includeFilters = @Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = InfluencerReadQueryDslRepository.class
    ),
    scripts = "/sql/test-influencer.sql"
)
class InfluencerReadQueryDslRepositoryTest extends AbstractMySQLContainer {

    @Autowired
    InfluencerReadQueryDslRepository influencerRepository;

    @Test
    void getInfluencerDetail() {
        // given
        Long influencerId = 2L;
        Long userId = 1L;
        InfluencerQueryResult.Detail expected = new InfluencerQueryResult.Detail(
            2L, "인플루언서2", "img2", "직업2", true, 4L, 0L
        );

        // when
        Optional<InfluencerQueryResult.Detail> actual = influencerRepository.getInfluencerDetail(influencerId, userId);

        //then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void getInfluencerNamesByPlaceId() {
        // given
        Long placeId = 1L;
        List<String> expected = List.of("인플루언서3", "인플루언서4");

        // when
        List<String> actual = influencerRepository.getInfluencerNamesByPlaceId(placeId);

        // then
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    void getInfluencerSortedByLikes() {
        // given
        Long userId = 1L;
        Pageable pageable = Pageable.ofSize(5);
        List<InfluencerQueryResult.Simple> expected = List.of(
            new InfluencerQueryResult.Simple(2L, "인플루언서2", "img2", "직업2", true),
            new InfluencerQueryResult.Simple(3L, "인플루언서3", "img3", "직업3", true),
            new InfluencerQueryResult.Simple(4L, "인플루언서4", "img4", "직업4", true),
            new InfluencerQueryResult.Simple(1L, "인플루언서1", "img1", "직업1", false)
        );
        // when
        Page<InfluencerQueryResult.Simple> actual = influencerRepository.getInfluencerSortedByLikes(userId, pageable);

        // then
        assertThat(actual.getTotalElements()).isEqualTo(expected.size());
        assertThat(actual.get()).containsExactlyElementsOf(expected);
    }
}
