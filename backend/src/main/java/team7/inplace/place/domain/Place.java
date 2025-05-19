package team7.inplace.place.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team7.inplace.global.baseEntity.BaseEntity;
import team7.inplace.place.application.command.PlacesCommand.Upsert;

@Getter
@Entity(name = "places")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    private Long categoryId;

    @Embedded
    private Address address;

    @Embedded
    private Coordinate coordinate;

    private String googlePlaceId;
    private Long kakaoPlaceId;

    public Place(
        String name, Long categoryId,
        String address, String x, String y, String googlePlaceId, Long kakaoPlaceId
    ) {
        this.name = name;
        this.categoryId = categoryId;
        this.address = Address.of(address);
        this.coordinate = Coordinate.of(x, y);
        this.googlePlaceId = googlePlaceId;
        this.kakaoPlaceId = kakaoPlaceId;
    }

    public void updateInfo(Upsert command) {
        this.name = command.placeName();
        this.categoryId = command.category();
        this.address = Address.of(command.address());
        this.coordinate = Coordinate.of(command.x(), command.y());
        this.googlePlaceId = command.googlePlaceId();
        this.kakaoPlaceId = command.kakaoPlaceId();
    }
}
