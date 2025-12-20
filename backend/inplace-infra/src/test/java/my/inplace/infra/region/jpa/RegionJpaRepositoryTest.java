package my.inplace.infra.region.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Optional;
import my.inplace.domain.region.Region;
import my.inplace.infra.config.AbstractMySQLContainer;
import my.inplace.infra.global.MySQLContainerJpaTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

@MySQLContainerJpaTest(
    includeFilters = @Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        value = RegionJpaRepository.class
    ),
    scripts = "/sql/test-region.sql"
)
class RegionJpaRepositoryTest extends AbstractMySQLContainer {

    @Autowired
    RegionJpaRepository regionJpaRepository;

    @Test
    @DisplayName("findById - Spatial data type Test")
    void findByIdTest() {
        //given
        Long id = 1L;
        Coordinate[] expectedCoordinates = {
            new CoordinateXY(0.0, 0.0),
            new CoordinateXY(0.0, 10.0),
            new CoordinateXY(10.0, 0.0),
            new CoordinateXY(10.0, 10.0),
            new CoordinateXY(0.0, 0.0),
        };

        //when
        Optional<Region> actual = regionJpaRepository.findById(id);

        //then
        assertThat(actual.isPresent()).isTrue();
        Coordinate[] actualCoordinates = actual.get().getArea().getCoordinates();
        for (int i = 0; i < actualCoordinates.length; i++) {
            assertThat(actualCoordinates[i]).isEqualTo(expectedCoordinates[i]);
        }
    }
}
