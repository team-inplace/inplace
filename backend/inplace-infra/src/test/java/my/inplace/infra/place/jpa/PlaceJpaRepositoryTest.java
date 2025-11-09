package my.inplace.infra.place.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Optional;
import my.inplace.domain.place.Place;
import my.inplace.domain.util.SingletonGeometryFactory;
import my.inplace.infra.config.AbstractMySQLContainer;
import my.inplace.infra.global.MySQLContainerJpaTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

@MySQLContainerJpaTest(
    includeFilters = @Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {PlaceJpaRepository.class, SingletonGeometryFactory.class}
    ),
    scripts = "/sql/test-place.sql"
)
class PlaceJpaRepositoryTest extends AbstractMySQLContainer {

    @Autowired
    private PlaceJpaRepository placeRepository;

    @Test
    @DisplayName("findById - Point class 잘 조회되는지 확인")
    void findById() {
        //given
        Long id = 1L;

        Point expected = SingletonGeometryFactory.newPoint(126.0, 36.0);
        //when
        Optional<Place> actual = placeRepository.findById(id);

        //then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getLocation().getCoordinate().equals2D(expected.getCoordinate())).isTrue();
    }

    @Test
    @DisplayName("save - Point class 잘 저장되는지 확인")
    void savePointTest() {
        //given
        Place placeToSave = new Place("testPlace", 1L, "address1 address2 address3", "38.0", "128.0", "googlePlaceId", 21L);

        //when
        placeRepository.saveAndFlush(placeToSave);
        long count = placeRepository.count();
        Optional<Place> actual = placeRepository.findById(count);

        //then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).usingRecursiveComparison()
            .ignoringFields("id", "createdAt")
            .isEqualTo(placeToSave);
        assertThat(actual.get().getId()).isEqualTo(count);
    }
}
