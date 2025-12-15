package my.inplace.domain.place;

import my.inplace.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.inplace.domain.util.SingletonGeometryFactory;
import org.locationtech.jts.geom.Point;

@Getter
@Entity(name = "places")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    private Long categoryId;

    @Embedded
    private Address address;

    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    private Point location;

    private String googlePlaceId;
    private Long kakaoPlaceId;

    public Place(
        String name, Long categoryId,
        String address, String x, String y, String googlePlaceId, Long kakaoPlaceId
    ) {
        this.name = name;
        this.categoryId = categoryId;
        this.address = Address.of(address);
        this.location = SingletonGeometryFactory.newPoint(Double.parseDouble(x), Double.parseDouble(y));
        this.googlePlaceId = googlePlaceId;
        this.kakaoPlaceId = kakaoPlaceId;
    }

    public void update(
        String name, Long categoryId,
        String address, String x, String y, String googlePlaceId, Long kakaoPlaceId
    ) {
        this.name = name;
        this.categoryId = categoryId;
        this.address = Address.of(address);
        this.location = SingletonGeometryFactory.newPoint(Double.parseDouble(x), Double.parseDouble(y));
        this.googlePlaceId = googlePlaceId;
        this.kakaoPlaceId = kakaoPlaceId;
    }
}
