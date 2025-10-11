package my.inplace.infra.place;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import my.inplace.domain.place.query.PlaceQueryParam;
import my.inplace.domain.place.query.PlaceQueryResult;
import my.inplace.domain.place.query.PlaceQueryResult.DetailedPlace;
import my.inplace.domain.place.query.PlaceQueryResult.SimplePlace;
import my.inplace.infra.config.AbstractMySQLContainer;
import my.inplace.infra.config.TestQueryDslConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@DataJpaTest(
    includeFilters = @Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = PlaceReadQueryDslRepository.class
    )
)
@ActiveProfiles("test-mysql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/test-place.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Import(TestQueryDslConfig.class)
@EntityScan("my.inplace.domain")
class PlaceReadQueryDslRepositoryTest extends AbstractMySQLContainer {

    @Autowired
    PlaceReadQueryDslRepository placeReadRepository;

    @Test
    void findDetailedPlaceById() {
        // given
        Long placeId = 1L;
        Long userId = 1L;
        PlaceQueryResult.DetailedPlace expected = new PlaceQueryResult.DetailedPlace(
            1L, "테스트장소1", "주소1-1", "주소2-1", "주소3", 126.0, 36.0, "카페", "googlePlaceId1", 1L, 1L, false
        );

        // when
        Optional<PlaceQueryResult.DetailedPlace> actual = placeReadRepository.findDetailedPlaceById(userId, placeId);

        // then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void findPlacesInMapRangeWithPagingWhenRegionExists() {
        // then
        PlaceQueryParam.Coordinate coordinate = new PlaceQueryParam.Coordinate( // 어차피 사용되지 않는다.
            null, null, null, null
        );
        PlaceQueryParam.Filter filter = new PlaceQueryParam.Filter(
            List.of(new PlaceQueryParam.Region("주소1", "주소2")),
            List.of(1L, 2L),
            List.of("인플루언서5")
        );
        Long userId = 1L;
        List<PlaceQueryResult.DetailedPlace> expected = List.of(
            new PlaceQueryResult.DetailedPlace(17L, "테스트장소17", "주소1", "주소2", "주소3", 127.6, 37.6, "카페", "googlePlaceId17", 17L, 1L,
                false),
            new PlaceQueryResult.DetailedPlace(18L, "테스트장소18", "주소1", "주소2", "주소3", 127.7, 37.7, "양식", "googlePlaceId18", 18L, 1L,
                true),
            new PlaceQueryResult.DetailedPlace(19L, "테스트장소19", "주소1", "주소2", "주소3", 127.8, 37.8, "일식", "googlePlaceId19", 19L, 1L,
                true),
            new PlaceQueryResult.DetailedPlace(20L, "테스트장소20", "주소1", "주소2", "주소3", 127.9, 37.9, "한식", "googlePlaceId20", 20L, 1L,
                true)
        );

        // when
        Page<DetailedPlace> actual = placeReadRepository.findPlacesInMapRangeWithPaging(
            coordinate,
            filter,
            Pageable.ofSize(5),
            userId
        );

        // then
        assertThat(actual.getTotalElements()).isEqualTo(expected.size());
        assertThat(actual.getContent()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void findPlacesInMapRangeWithPagingWhenRegionDoesNotExist() {
        // then
        PlaceQueryParam.Coordinate coordinate = new PlaceQueryParam.Coordinate( // 어차피 사용되지 않는다.
            126.2, 37.5, 127.5, 36.2
        );
        PlaceQueryParam.Filter filter = new PlaceQueryParam.Filter(
            List.of(),
            List.of(1L, 2L),
            List.of("인플루언서3")
        );
        Long userId = 1L;
        List<PlaceQueryResult.DetailedPlace> expected = List.of(
            new PlaceQueryResult.DetailedPlace(9L, "테스트장소9", "주소1-2", "주소2-3", "주소3", 126.8, 36.8, "일식", "googlePlaceId9", 9L, 1L,
                false),
            new PlaceQueryResult.DetailedPlace(10L, "테스트장소10", "주소1-2", "주소2-3", "주소3", 126.9, 36.9, "한식", "googlePlaceId10", 10L,
                1L, false),
            new PlaceQueryResult.DetailedPlace(11L, "테스트장소11", "주소1", "주소2", "주소3", 127.0, 37.0, "카페", "googlePlaceId11", 11L, 1L,
                false),
            new PlaceQueryResult.DetailedPlace(12L, "테스트장소12", "주소1", "주소2", "주소3", 127.1, 37.1, "카페", "googlePlaceId12", 12L, 1L,
                true)
        );

        // when
        Page<DetailedPlace> actual = placeReadRepository.findPlacesInMapRangeWithPaging(
            coordinate,
            filter,
            Pageable.ofSize(5),
            userId
        );

        // then
        assertThat(actual.getTotalElements()).isEqualTo(expected.size());
        assertThat(actual.getContent()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void findPlaceLocationsInMapRangeWhenRegionExists() {
        // given
        PlaceQueryParam.Coordinate coordinate = new PlaceQueryParam.Coordinate( // 어차피 사용되지 않는다.
            null, null, null, null
        );
        PlaceQueryParam.Filter filter = new PlaceQueryParam.Filter(
            List.of(new PlaceQueryParam.Region("주소1", "주소2")),
            List.of(1L, 2L),
            List.of("인플루언서3")
        );
        List<PlaceQueryResult.Marker> expected = List.of(
            new PlaceQueryResult.Marker(11L, "eats", 127.0, 37.0),
            new PlaceQueryResult.Marker(12L, "eats", 127.1, 37.1)
        );

        // when
        List<PlaceQueryResult.Marker> actual = placeReadRepository.findPlaceLocationsInMapRange(coordinate, filter);
        // then

        assertThat(actual).isNotNull();
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    void findPlaceLocationsInMapRangeWhenRegionDoesNotExist() {
        // given
        PlaceQueryParam.Coordinate coordinate = new PlaceQueryParam.Coordinate( // 어차피 사용되지 않는다.
            126.2, 37.5, 127.5, 36.2
        );
        PlaceQueryParam.Filter filter = new PlaceQueryParam.Filter(
            List.of(),
            List.of(1L, 2L),
            List.of("인플루언서4")
        );
        List<PlaceQueryResult.Marker> expected = List.of(
            new PlaceQueryResult.Marker(13L, "eats", 127.2, 37.2),
            new PlaceQueryResult.Marker(14L, "eats", 127.3, 37.3),
            new PlaceQueryResult.Marker(15L, "eats", 127.4, 37.4),
            new PlaceQueryResult.Marker(16L, "eats", 127.5, 37.5)
        );

        // when
        List<PlaceQueryResult.Marker> actual = placeReadRepository.findPlaceLocationsInMapRange(coordinate, filter);
        // then

        assertThat(actual).isNotNull();
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    void findSimplePlaceById() {
        // given
        Long placeId = 1L;
        PlaceQueryResult.SimplePlace expected = new PlaceQueryResult.SimplePlace(1L, "테스트장소1", "주소1-1", "주소2-1", "주소3");
        // when
        Optional<SimplePlace> actual = placeReadRepository.findSimplePlaceById(placeId);
        // then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void findLikedPlacesByUserIdWithPaging() {
        // given
        Long userId = 1L;
        Pageable pageable = Pageable.ofSize(5);
        List<PlaceQueryResult.DetailedPlace> expected = List.of(
            new PlaceQueryResult.DetailedPlace(12L, "테스트장소12", "주소1", "주소2", "주소3", 127.1, 37.1, "카페", "googlePlaceId12", 12L, 1L,
                true),
            new PlaceQueryResult.DetailedPlace(13L, "테스트장소13", "주소1", "주소2", "주소3", 127.2, 37.2, "양식", "googlePlaceId13", 13L, 1L,
                true),
            new PlaceQueryResult.DetailedPlace(18L, "테스트장소18", "주소1", "주소2", "주소3", 127.7, 37.7, "양식", "googlePlaceId18", 18L, 1L,
                true),
            new PlaceQueryResult.DetailedPlace(19L, "테스트장소19", "주소1", "주소2", "주소3", 127.8, 37.8, "일식", "googlePlaceId19", 19L, 1L,
                true),
            new PlaceQueryResult.DetailedPlace(20L, "테스트장소20", "주소1", "주소2", "주소3", 127.9, 37.9, "한식", "googlePlaceId20", 20L, 1L,
                true)
        );

        // when
        Page<PlaceQueryResult.DetailedPlace> actual = placeReadRepository.findLikedPlacesByUserIdWithPaging(userId, pageable);

        // then
        assertThat(actual.getTotalElements()).isEqualTo(expected.size());
        assertThat(actual.getContent()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void findPlaceMarkerById() {
        // given
        Long placeId = 1L;
        PlaceQueryResult.MarkerDetail expected = new PlaceQueryResult.MarkerDetail(1L, "테스트장소1", "카페", "주소1-1", "주소2-1", "주소3");

        // when
        PlaceQueryResult.MarkerDetail actual = placeReadRepository.findPlaceMarkerById(placeId);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findPlaceLocationsByNameWhenRegionExists() {
        // given
        String name = "테스트장소";
        PlaceQueryParam.Filter filter = new PlaceQueryParam.Filter(
            List.of(new PlaceQueryParam.Region("주소1-1", "주소2-2")),
            List.of(1L, 2L),
            List.of("인플루언서2")
        );
        List<PlaceQueryResult.Marker> expected = List.of(
            new PlaceQueryResult.Marker(6L, "eats", 126.5, 36.5)
        );

        // when
        List<PlaceQueryResult.Marker> actual = placeReadRepository.findPlaceLocationsByName(name, filter);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    void findPlaceLocationsByNameWhenRegionDoesNotExist() {
        // given
        String name = "테스트장소";
        PlaceQueryParam.Filter filter = new PlaceQueryParam.Filter(
            List.of(),
            List.of(1L, 2L),
            List.of("인플루언서4")
        );
        List<PlaceQueryResult.Marker> expected = List.of(
            new PlaceQueryResult.Marker(13L, "eats", 127.2, 37.2),
            new PlaceQueryResult.Marker(14L, "eats", 127.3, 37.3),
            new PlaceQueryResult.Marker(15L, "eats", 127.4, 37.4),
            new PlaceQueryResult.Marker(16L, "eats", 127.5, 37.5)
        );

        // when
        List<PlaceQueryResult.Marker> actual = placeReadRepository.findPlaceLocationsByName(name, filter);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    void findPlacesByNameWithPagingWithRegionExists() {
        // given
        Long userId = 1L;
        String name = "소1";
        PlaceQueryParam.Filter filter = new PlaceQueryParam.Filter(
            List.of(new PlaceQueryParam.Region("주소1-1", "주소2-1")),
            List.of(1L, 2L),
            List.of("인플루언서1")
        );
        Pageable pageable = Pageable.ofSize(5);
        List<PlaceQueryResult.DetailedPlace> expected = List.of(
            new PlaceQueryResult.DetailedPlace(1L, "테스트장소1", "주소1-1", "주소2-1", "주소3", 126.0, 36.0, "카페", "googlePlaceId1", 1L, 1L, false)
        );

        // when
        Page<PlaceQueryResult.DetailedPlace> actual = placeReadRepository.findPlacesByNameWithPaging(userId, name, filter,
            pageable);

        // then
        assertThat(actual.getTotalElements()).isEqualTo(expected.size());
    }

    @Test
    void findPlacesByNameWithPagingWithRegionDoesNotExist() {
        // given
        Long userId = 1L;
        String name = "소20";
        PlaceQueryParam.Filter filter = new PlaceQueryParam.Filter(
            List.of(),
            List.of(1L, 2L),
            List.of("인플루언서5")
        );
        Pageable pageable = Pageable.ofSize(5);
        List<PlaceQueryResult.DetailedPlace> expected = List.of(
            new PlaceQueryResult.DetailedPlace(20L, "테스트장소20", "주소1", "주소2", "주소3", 127.9, 37.9, "한식", "googlePlaceId20", 20L, 1L, true)
            );

        // when
        Page<PlaceQueryResult.DetailedPlace> actual = placeReadRepository.findPlacesByNameWithPaging(userId, name, filter,
            pageable);

        // then
        assertThat(actual.getTotalElements()).isEqualTo(expected.size());
    }

    @Test
    void getDetailedPlacesByVideoId() {
        // given
        Long videoId = 1L;
        List<PlaceQueryResult.DetailedPlace> expected = List.of(
            new PlaceQueryResult.DetailedPlace(1L, "테스트장소1", "주소1-1", "주소2-1", "주소3", 126.0, 36.0, "카페", "googlePlaceId1", 1L, null, null),
            new PlaceQueryResult.DetailedPlace(20L, "테스트장소20", "주소1", "주소2", "주소3", 127.9, 37.9, "한식", "googlePlaceId20", 20L, null, null)

        );

        // when
        List<PlaceQueryResult.DetailedPlace> actual = placeReadRepository.getDetailedPlacesByVideoId(videoId);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
