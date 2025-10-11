package my.inplace.domain.place;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Coordinate {

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double latitude;

    private Coordinate(String longitude, String latitude) {
        this.longitude = Double.valueOf(longitude);
        this.latitude = Double.valueOf(latitude);
    }

    public static Coordinate of(String longitude, String latitude) {
        return new Coordinate(longitude, latitude);
    }
}
