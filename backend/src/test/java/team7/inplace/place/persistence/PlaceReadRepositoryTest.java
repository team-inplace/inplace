package team7.inplace.place.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import team7.inplace.place.application.command.PlacesCommand.RegionParam;
import team7.inplace.place.domain.Category;
import team7.inplace.place.persistence.dto.PlaceQueryResult.DetailedPlace;

@DataJpaTest
@ActiveProfiles("test")
@Sql("/sql/test-place.sql")
class PlaceReadRepositoryTest {

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
        List<RegionParam> regionParams = List.of(RegionParam.of("주소1-1", "주소2-1"));
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
        List<RegionParam> regionParams = List.of(RegionParam.of("주소1-1", null));
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
            RegionParam.of("주소1-1", "주소2-1"),
            RegionParam.of("주소1-2", "주소2-2"));
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
        List<RegionParam> regionParams = List.of(RegionParam.of("주소1-1", null));
        List<String> influencerFilters = List.of("인플루언서1");
        List<Category> category = List.of(Category.RESTAURANT, Category.CAFE);
        Pageable pageable = PageRequest.of(0, 5);
        Long userId = null;

        final int expectedTotalContent = 2;
        final int expectedContentSize = 2;
        final List<Long> expectedPlaceIds = List.of(1L, 2L);

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
        List<Category> category = List.of(Category.CAFE);

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
        List<Category> category = List.of(Category.CAFE, Category.RESTAURANT);

        final int expectedTotalContent = 8;
        final int expectedContentSize = 5;
        final List<Long> expectedPlaceIds = List.of(1L, 2L, 6L, 7L, 11L);

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
        List<String> influencerName = List.of("인플루언서1");

        final int expectedTotalContent = 4;
        final int expectedContentSize = 4;
        final List<Long> expectedPlaceIds = List.of(1L, 2L, 3L, 4L);

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
        List<String> influencerName = List.of("인플루언서1", "인플루언서2");

        final int expectedTotalContent = 8;
        final int expectedContentSize = 5;
        final List<Long> expectedPlaceIds = List.of(1L, 2L, 3L, 4L, 5L);

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
        List<Category> category = List.of(Category.CAFE);

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
        List<Category> category = List.of(Category.CAFE, Category.RESTAURANT);

        final int expectedTotalContent = 5;
        final int expectedContentSize = 5;
        final List<Long> expectedPlaceIds = List.of(6L, 7L, 11L, 12L, 16L);

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
        List<Category> category = List.of(Category.CAFE);
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
}