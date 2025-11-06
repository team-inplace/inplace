package my.inplace.domain.region;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.inplace.domain.base.BaseEntity;
import org.locationtech.jts.geom.Geometry;

@Getter
@Entity(name = "regions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region extends BaseEntity {

    @Column(length = 10)
    private String city;

    @Column(length = 10)
    private String district;

    @Column(nullable = false, columnDefinition = "GEOMETRY SRID 4326")
    private Geometry area;
}
