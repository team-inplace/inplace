package team7.inplace.place.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import team7.inplace.container.AbstractMySQLContainerTest;
import team7.inplace.place.application.command.PlacesCommand.RegionParam;
import team7.inplace.place.persistence.dto.PlaceQueryResult.DetailedPlace;
import team7.inplace.place.persistence.dto.PlaceQueryResult.Marker;

@DataJpaTest
@ActiveProfiles("test-mysql")
@Import(ObjectMapper.class)
@Sql(scripts = {"/sql/test-place.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlaceReadRepositoryTest extends AbstractMySQLContainerTest {

    @Autowired
    private TestEntityManager testEntityManager;
    private PlaceReadRepository placeReadRepository;

    /**
     * 테스트 데이터 : 총 20개의 장소 좌표 범위 : (126.0, 36.0) ~ (128.0, 38.0) 자세한 내용은 src/test/resources/sql/test-place.sql 참고
     */
    @BeforeEach
    void setUp() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(testEntityManager.getEntityManager());
        this.placeReadRepository = new PlaceReadRepositoryImpl(queryFactory);
    }

    @Test
    @DisplayName("장소 조회 테스트 - 위치 필터링 (도시-구)")
    void findPlace_RegionFiltering_City_District() {
        // given
        // 바운더리는 있어도 영향 안줌
        Double topLeftLongitude = 126.5;
        Double topLeftLatitude = 37.5;
        Double bottomRightLongitude = 127.4;
        Double bottomRightLatitude = 36.4;
        Double longitude = 127.0;
        Double latitude = 37.0;
        List<RegionParam> regionParams = List.of(new RegionParam("주소1-1", "주소2-1"));
        Pageable pageable = PageRequest.of(0, 5);
        Long userId = null;

        final int expectedTotalContent = 5;
        final int expectedContentSize = 5;
        final List<Long> expectedPlaceIds = List.of(1L, 2L, 3L, 4L, 5L);

        // when
        Page<DetailedPlace> places = placeReadRepository.findPlacesInMapRangeWithPaging(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude,
            latitude,
            regionParams, null, null, pageable, userId
        );

        // then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(places.getContent().stream().map(DetailedPlace::placeId).toList())
            .isEqualTo(expectedPlaceIds);
    }

    @Test
    @DisplayName("장소 조회 테스트 - 위치 필터링 (도시-전체)")
    void findPlace_RegionFiltering_City_All() {
        // given
        // 바운더리는 있어도 영향 안줌
        Double topLeftLongitude = 126.5;
        Double topLeftLatitude = 37.5;
        Double bottomRightLongitude = 127.4;
        Double bottomRightLatitude = 36.4;
        Double longitude = 127.0;
        Double latitude = 37.0;
        List<RegionParam> regionParams = List.of(new RegionParam("주소1-1", null));
        Pageable pageable = PageRequest.of(0, 10);
        Long userId = null;

        final int expectedTotalContent = 6;
        final int expectedContentSize = 6;
        final List<Long> expectedPlaceIds = List.of(1L, 2L, 3L, 4L, 5L, 6L);

        // when
        Page<DetailedPlace> places = placeReadRepository.findPlacesInMapRangeWithPaging(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude,
            latitude,
            regionParams, null, null, pageable, userId
        );

        // then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(places.getContent().stream().map(DetailedPlace::placeId).toList())
            .isEqualTo(expectedPlaceIds);
    }

    @Test
    @DisplayName("장소 조회 테스트 - 위치 필터링 (도시-구) 여러 개")
    void findPlace_RegionFiltering_City_District_Many() {
        // given
        // 바운더리는 있어도 영향 안줌
        Double topLeftLongitude = 126.5;
        Double topLeftLatitude = 37.5;
        Double bottomRightLongitude = 127.4;
        Double bottomRightLatitude = 36.4;
        Double longitude = 127.0;
        Double latitude = 37.0;
        List<RegionParam> regionParams = List.of(
            new RegionParam("주소1-1", "주소2-1"),
            new RegionParam("주소1-2", "주소2-2"));
        Pageable pageable = PageRequest.of(0, 10);
        Long userId = null;

        final int expectedTotalContent = 7;
        final int expectedContentSize = 7;
        final List<Long> expectedPlaceIds = List.of(1L, 2L, 3L, 4L, 5L, 7L, 8L);

        // when
        Page<DetailedPlace> places = placeReadRepository.findPlacesInMapRangeWithPaging(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude,
            latitude,
            regionParams, null, null, pageable, userId
        );

        // then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(places.getContent().stream().map(DetailedPlace::placeId).toList())
            .isEqualTo(expectedPlaceIds);
    }

    @Test
    @DisplayName("장소 조회 테스트 - 위치 필터링 (도시-전체) + 인플루언서 필터링 + 카테고리 필터링")
    void findPlace_RegionFiltering_InfluencerFiltering_CategoryFiltering() {
        // given
        // 바운더리는 있어도 영향 안줌
        Double topLeftLongitude = 126.5;
        Double topLeftLatitude = 37.5;
        Double bottomRightLongitude = 127.4;
        Double bottomRightLatitude = 36.4;
        Double longitude = 127.0;
        Double latitude = 37.0;
        List<RegionParam> regionParams = List.of(new RegionParam("주소1-1", null));
        List<String> influencerFilters = List.of("인플루언서1");
        List<Long> category = List.of(2L, 3L);
        Pageable pageable = PageRequest.of(0, 5);
        Long userId = null;

        final int expectedTotalContent = 2;
        final int expectedContentSize = 2;
        final List<Long> expectedPlaceIds = List.of(2L, 3L);

        // when
        Page<DetailedPlace> places = placeReadRepository.findPlacesInMapRangeWithPaging(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude,
            latitude,
            regionParams, category, influencerFilters, pageable, userId
        );

        // then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(places.getContent().stream().map(DetailedPlace::placeId).toList())
            .isEqualTo(expectedPlaceIds);
    }

    @Test
    @DisplayName("장소 조회 테스트 - 범위 내에 있는 장소 조회")
    void findPlace_InRange_NoFiltering() {
        // given
        Double topLeftLongitude = 126.5;
        Double topLeftLatitude = 37.5;
        Double bottomRightLongitude = 127.4;
        Double bottomRightLatitude = 36.4;
        Double longitude = 127.0;
        Double latitude = 37.0;
        Pageable pageable = PageRequest.of(0, 5);
        Long userId = null;

        final int expectedTotalContent = 10;
        final int expectedContentSize = 5;
        final List<Long> expectedPlaceIds = List.of(6L, 7L, 8L, 9L, 10L);

        // when
        Page<DetailedPlace> places = placeReadRepository.findPlacesInMapRangeWithPaging(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude,
            latitude,
            null, null, null, pageable, userId
        );

        // then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(places.getContent().stream().map(DetailedPlace::placeId).toList())
            .isEqualTo(expectedPlaceIds);
    }

    @Test
    @DisplayName("장소 조회 테스트 - 카테고리 필터링(한개)")
    void findPlaces_CategoryFiltering_One() {
        // given
        Double topLeftLongitude = 126.0;
        Double topLeftLatitude = 38.0;
        Double bottomRightLongitude = 128.0;
        Double bottomRightLatitude = 36.0;
        Double longitude = 127.0;
        Double latitude = 37.0;
        Pageable pageable = PageRequest.of(0, 5);
        Long userId = null;
        List<Long> category = List.of(2L);

        final int expectedTotalContent = 4;
        final int expectedContentSize = 4;
        final List<Long> expectedPlaceIds = List.of(2L, 7L, 12L, 17L);

        // when
        Page<DetailedPlace> places = placeReadRepository.findPlacesInMapRangeWithPaging(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude,
            latitude,
            null, category, null, pageable, userId
        );

        // then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(places.getContent().stream().map(DetailedPlace::placeId).toList())
            .isEqualTo(expectedPlaceIds);
    }

    @Test
    @DisplayName("장소 조회 테스트 - 카테고리 필터링(여러개)")
    void findPlaces_CategoryFiltering_Many() {
        // given
        Double topLeftLongitude = 126.0;
        Double topLeftLatitude = 38.0;
        Double bottomRightLongitude = 128.0;
        Double bottomRightLatitude = 36.0;
        Double longitude = 127.0;
        Double latitude = 37.0;
        Pageable pageable = PageRequest.of(0, 5);
        Long userId = null;
        List<Long> category = List.of(2L, 3L);

        final int expectedTotalContent = 8;
        final int expectedContentSize = 5;
        final List<Long> expectedPlaceIds = List.of(2L, 3L, 7L, 8L, 12L);

        // when
        Page<DetailedPlace> places = placeReadRepository.findPlacesInMapRangeWithPaging(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude,
            latitude,
            null, category, null, pageable, userId
        );

        // then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(places.getContent().stream().map(DetailedPlace::placeId).toList())
            .isEqualTo(expectedPlaceIds);
    }

    @Test
    @DisplayName("장소 조회 테스트 - 인플루언서 이름 필터링(한개)")
    void findPlaces_InfluencerFiltering_One() {
        // given
        Double topLeftLongitude = 126.0;
        Double topLeftLatitude = 38.0;
        Double bottomRightLongitude = 128.0;
        Double bottomRightLatitude = 36.0;
        Double longitude = 127.0;
        Double latitude = 37.0;
        Pageable pageable = PageRequest.of(0, 5);
        Long userId = null;
        List<String> influencerName = List.of("인플루언서2");

        final int expectedTotalContent = 4;
        final int expectedContentSize = 4;
        final List<Long> expectedPlaceIds = List.of(5L, 6L, 7L, 8L);

        // when
        Page<DetailedPlace> places = placeReadRepository.findPlacesInMapRangeWithPaging(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude,
            latitude,
            null, null, influencerName, pageable, userId
        );

        // then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(places.getContent().stream().map(DetailedPlace::placeId).toList())
            .isEqualTo(expectedPlaceIds);
    }

    @Test
    @DisplayName("장소 조회 테스트 - 인플루언서 이름 필터링(여러개)")
    void findPlaces_InfluencerFiltering_Many() {
        // given
        Double topLeftLongitude = 126.0;
        Double topLeftLatitude = 38.0;
        Double bottomRightLongitude = 128.0;
        Double bottomRightLatitude = 36.0;
        Double longitude = 127.0;
        Double latitude = 37.0;
        Pageable pageable = PageRequest.of(0, 5);
        Long userId = null;
        List<String> influencerName = List.of("인플루언서2", "인플루언서3");

        final int expectedTotalContent = 8;
        final int expectedContentSize = 5;
        final List<Long> expectedPlaceIds = List.of(5L, 6L, 7L, 8L, 9L);

        // when
        Page<DetailedPlace> places = placeReadRepository.findPlacesInMapRangeWithPaging(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude,
            latitude,
            null, null, influencerName, pageable, userId
        );

        // then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(places.getContent().stream().map(DetailedPlace::placeId).toList())
            .isEqualTo(expectedPlaceIds);
    }

    @Test
    @DisplayName("장소 조회 테스트 - 범위 내에 있는 장소 조회 + 카테고리 필터링(한개)")
    void findPlaces_InRange_CategoryFiltering_One() {
        // given
        Double topLeftLongitude = 126.5;
        Double topLeftLatitude = 37.5;
        Double bottomRightLongitude = 127.5;
        Double bottomRightLatitude = 36.5;
        Double longitude = 127.0;
        Double latitude = 37.0;
        Pageable pageable = PageRequest.of(0, 5);
        Long userId = null;
        List<Long> category = List.of(2L);

        final int expectedTotalContent = 2;
        final int expectedContentSize = 2;
        final List<Long> expectedPlaceIds = List.of(7L, 12L);

        // when
        Page<DetailedPlace> places = placeReadRepository.findPlacesInMapRangeWithPaging(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude,
            latitude,
            null, category, null, pageable, userId
        );

        // then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(places.getContent().stream().map(DetailedPlace::placeId).toList())
            .isEqualTo(expectedPlaceIds);
    }

    @Test
    @DisplayName("장소 조회 테스트 - 범위 내에 있는 장소 조회 + 카테고리 필터링(여러개)")
    void findPlaces_InRange_CategoryFiltering_Many() {
        // given
        Double topLeftLongitude = 126.5;
        Double topLeftLatitude = 37.5;
        Double bottomRightLongitude = 127.5;
        Double bottomRightLatitude = 36.5;
        Double longitude = 127.0;
        Double latitude = 37.0;
        Pageable pageable = PageRequest.of(0, 5);
        Long userId = null;
        List<Long> category = List.of(2L, 3L);

        final int expectedTotalContent = 4;
        final int expectedContentSize = 4;
        final List<Long> expectedPlaceIds = List.of(7L, 8L, 12L, 13L);

        // when
        Page<DetailedPlace> places = placeReadRepository.findPlacesInMapRangeWithPaging(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude,
            latitude,
            null, category, null, pageable, userId
        );

        // then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(places.getContent().stream().map(DetailedPlace::placeId).toList())
            .isEqualTo(expectedPlaceIds);
    }

    @Test
    @DisplayName("장소 조회 테스트 - 범위 내에 있는 장소 조회 + 인플루언서 이름 필터링(한개)")
    void findPlaces_InRange_InfluencerFiltering_One() {
        // given
        Double topLeftLongitude = 126.5;
        Double topLeftLatitude = 37.5;
        Double bottomRightLongitude = 127.5;
        Double bottomRightLatitude = 36.5;
        Double longitude = 127.0;
        Double latitude = 37.0;
        Pageable pageable = PageRequest.of(0, 5);
        Long userId = null;
        List<String> influencerName = List.of("인플루언서2");

        final int expectedTotalContent = 3;
        final int expectedContentSize = 3;
        final List<Long> expectedPlaceIds = List.of(6L, 7L, 8L);

        // when
        Page<DetailedPlace> places = placeReadRepository.findPlacesInMapRangeWithPaging(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude,
            latitude,
            null, null, influencerName, pageable, userId
        );

        // then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(places.getContent().stream().map(DetailedPlace::placeId).toList())
            .isEqualTo(expectedPlaceIds);
    }

    @Test
    @DisplayName("장소 조회 테스트 - 범위 내에 있는 장소 조회 + 인플루언서 이름 필터링(여러개)")
    void findPlaces_InRange_InfluencerFiltering_Many() {
        // given
        Double topLeftLongitude = 126.5;
        Double topLeftLatitude = 37.5;
        Double bottomRightLongitude = 127.5;
        Double bottomRightLatitude = 36.5;
        Double longitude = 127.0;
        Double latitude = 37.0;
        Pageable pageable = PageRequest.of(0, 5);
        Long userId = null;
        List<String> influencerName = List.of("인플루언서2", "인플루언서3");

        final int expectedTotalContent = 7;
        final int expectedContentSize = 5;
        final List<Long> expectedPlaceIds = List.of(6L, 7L, 8L, 9L, 10L);

        // when
        Page<DetailedPlace> places = placeReadRepository.findPlacesInMapRangeWithPaging(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude,
            latitude,
            null, null, influencerName, pageable, userId
        );

        // then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(places.getContent().stream().map(DetailedPlace::placeId).toList())
            .isEqualTo(expectedPlaceIds);
    }

    @Test
    @DisplayName("장소 조회 테스트 - 범위 내에 있는 장소 조회 + 카테고리 필터링(한개) + 인플루언서 이름 필터링(한개)")
    void findPlaces_InRange_CategoryFiltering_One_InfluencerFiltering_One() {
        // given
        Double topLeftLongitude = 126.5;
        Double topLeftLatitude = 37.5;
        Double bottomRightLongitude = 127.5;
        Double bottomRightLatitude = 36.5;
        Double longitude = 127.0;
        Double latitude = 37.0;
        Pageable pageable = PageRequest.of(0, 5);
        Long userId = null;
        List<Long> category = List.of(2L);
        List<String> influencerName = List.of("인플루언서2");

        final int expectedTotalContent = 1;
        final int expectedContentSize = 1;
        final List<Long> expectedPlaceIds = List.of(7L);

        // when
        Page<DetailedPlace> places = placeReadRepository.findPlacesInMapRangeWithPaging(
            topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude, longitude,
            latitude,
            null, category, influencerName, pageable, userId
        );

        // then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().size()).isEqualTo(expectedContentSize);
        assertThat(places.getContent().stream().map(DetailedPlace::placeId).toList())
            .isEqualTo(expectedPlaceIds);
    }

    @Test
    @DisplayName("이름으로 장소 위치 조회 테스트 - 필터링 없음")
    void findPlaceByName_NoFiltering() {
        // given
        String name = "테스트장소10";
        final int expectedTotalContent = 1;
        final Long expectedPlaceId = 10L;
        final Double expectedLongitude = 126.9;
        final Double expectedLatitude = 36.9;

        // when
        var places = placeReadRepository.findPlaceLocationsByName(
            name,
            null,
            null,
            null
        );

        //then
        assertThat(places.size()).isEqualTo(expectedTotalContent);
        assertThat(places.get(0).placeId()).isEqualTo(expectedPlaceId);
        assertThat(places.get(0).longitude()).isEqualTo(expectedLongitude);
        assertThat(places.get(0).latitude()).isEqualTo(expectedLatitude);
    }

    @Test
    @DisplayName("이름으로 장소 위치 조회 테스트 - 도시-구 필터링")
    void findPlaceByName_RegionFiltering_City_District() {
        // given
        String name = "테스트장소";
        List<RegionParam> regionParams = List.of(new RegionParam("주소1-1", "주소2-1"));
        final List<Long> ids = List.of(1L, 2L, 3L, 4L, 5L);
        final int expectedTotalContent = 5;

        // when
        var places = placeReadRepository.findPlaceLocationsByName(
            name,
            regionParams,
            null,
            null
        );

        //then
        assertThat(places.size()).isEqualTo(expectedTotalContent);
        assertThat(places.stream().map(Marker::placeId).toList()).isEqualTo(ids);
    }

    @Test
    @DisplayName("이름으로 장소 위치 조회 테스트 - 도시-전체 필터링")
    void findPlaceByName_RegionFiltering_City_All() {
        // given
        String name = "테스트장소";
        List<RegionParam> regionParams = List.of(new RegionParam("주소1-1", null));
        final List<Long> ids = List.of(1L, 2L, 3L, 4L, 5L, 6L);
        final int expectedTotalContent = 6;

        // when
        var places = placeReadRepository.findPlaceLocationsByName(
            name,
            regionParams,
            null,
            null
        );

        //then
        assertThat(places.size()).isEqualTo(expectedTotalContent);
        assertThat(places.stream().map(Marker::placeId).toList()).isEqualTo(ids);
    }

    @Test
    @DisplayName("이름으로 장소 위치 조회 테스트 - 도시-구 여러 개 필터링")
    void findPlaceByName_RegionFiltering_City_District_Many() {
        // given
        String name = "테스트장소";
        List<RegionParam> regionParams = List.of(
            new RegionParam("주소1-1", "주소2-1"),
            new RegionParam("주소1-2", "주소2-2"));
        final List<Long> ids = List.of(1L, 2L, 3L, 4L, 5L, 7L, 8L);
        final int expectedTotalContent = 7;

        // when
        var places = placeReadRepository.findPlaceLocationsByName(
            name,
            regionParams,
            null,
            null
        );

        //then
        assertThat(places.size()).isEqualTo(expectedTotalContent);
        assertThat(places.stream().map(Marker::placeId).toList()).isEqualTo(ids);
    }

    @Test
    @DisplayName("이름으로 장소 리스트 조회 테스트 - 필터링 없음")
    void findPlacesByName_NoFiltering() {
        // given
        String name = "테스트장소";
        Pageable pageable = PageRequest.of(0, 5);
        final List<Long> ids = List.of(1L, 2L, 3L, 4L, 5L);
        final int expectedTotalContent = 20;

        // when
        var places = placeReadRepository.findPlacesByNameWithPaging(
            null,
            name,
            null,
            null,
            null,
            pageable
        );

        //then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().stream()
            .map(DetailedPlace::placeId).toList())
            .isEqualTo(ids);
    }

    @Test
    @DisplayName("이름으로 장소 리스트 조회 테스트 - 도시-구 필터링")
    void findPlacesByName_RegionFiltering_City_District() {
        // given
        String name = "테스트장소";
        List<RegionParam> regionParams = List.of(new RegionParam("주소1-1", "주소2-1"));
        Pageable pageable = PageRequest.of(0, 5);
        final List<Long> ids = List.of(1L, 2L, 3L, 4L, 5L);
        final int expectedTotalContent = 5;

        // when
        var places = placeReadRepository.findPlacesByNameWithPaging(
            null,
            name,
            regionParams,
            null,
            null,
            pageable
        );

        //then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().stream()
            .map(DetailedPlace::placeId).toList())
            .isEqualTo(ids);
    }

    @Test
    @DisplayName("이름으로 장소 리스트 조회 테스트 - 도시-전체 필터링")
    void findPlacesByName_RegionFiltering_City_All() {
        // given
        String name = "테스트장소";
        List<RegionParam> regionParams = List.of(new RegionParam("주소1", null));
        Pageable pageable = PageRequest.of(0, 5);
        final List<Long> ids = List.of(11L, 12L, 13L, 14L, 15L);
        final int expectedTotalContent = 10;

        // when
        var places = placeReadRepository.findPlacesByNameWithPaging(
            null,
            name,
            regionParams,
            null,
            null,
            pageable
        );

        //then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().stream()
            .map(DetailedPlace::placeId).toList())
            .isEqualTo(ids);
    }

    @Test
    @DisplayName("이름으로 장소 리스트 조회 테스트 - 도시-구 여러 개 필터링")
    void findPlacesByName_RegionFiltering_City_District_Many() {
        // given
        String name = "테스트장소";
        List<RegionParam> regionParams = List.of(
            new RegionParam("주소1-1", "주소2-1"),
            new RegionParam("주소1-2", "주소2-2"));
        Pageable pageable = PageRequest.of(0, 5);
        final List<Long> ids = List.of(1L, 2L, 3L, 4L, 5L);
        final int expectedTotalContent = 7;

        // when
        var places = placeReadRepository.findPlacesByNameWithPaging(
            null,
            name,
            regionParams,
            null,
            null,
            pageable
        );

        //then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().stream()
            .map(DetailedPlace::placeId).toList())
            .isEqualTo(ids);
    }

    @Test
    @DisplayName("이름으로 장소 조회 테스트 - 카테고리 필터링(한개)")
    void findPlacesByName_CategoryFiltering_One() {
        // given
        String name = "테스트장소";
        List<Long> categories = List.of(2L);
        Pageable pageable = PageRequest.of(0, 5);
        final List<Long> ids = List.of(2L, 7L, 12L, 17L);
        final int expectedTotalContent = 4;

        // when
        var places = placeReadRepository.findPlacesByNameWithPaging(
            null,
            name,
            null,
            categories,
            null,
            pageable
        );

        //then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().stream()
            .map(DetailedPlace::placeId).toList())
            .isEqualTo(ids);
    }

    @Test
    @DisplayName("이름으로 장소 조회 테스트 - 카테고리(한개) + 도시-구 필터링")
    void findPlacesByName_CategoryFiltering_One_RegionFiltering_City_District() {
        // given
        String name = "테스트장소";
        List<Long> categories = List.of(2L);
        List<RegionParam> regionParams = List.of(new RegionParam("주소1-1", "주소2-1"));
        Pageable pageable = PageRequest.of(0, 5);
        final List<Long> ids = List.of(2L);
        final int expectedTotalContent = 1;

        // when
        var places = placeReadRepository.findPlacesByNameWithPaging(
            null,
            name,
            regionParams,
            categories,
            null,
            pageable
        );

        //then
        assertThat(places.getTotalElements()).isEqualTo(expectedTotalContent);
        assertThat(places.getContent().stream()
            .map(DetailedPlace::placeId).toList())
            .isEqualTo(ids);
    }

    @Test
    @DisplayName("videoId로 장소 조회 테스트")
    void PlaceReadRepositoryTest() {
        // given
        Long videoId = 1L;
        List<Long> expectedPlaceIds = List.of(1L, 20L);
        // when
        List<DetailedPlace> places = placeReadRepository.getDetailedPlacesByVideoId(videoId);
        // then
        assertThat(places.stream().map(DetailedPlace::placeId).toList()).isEqualTo(expectedPlaceIds);
    }
}
