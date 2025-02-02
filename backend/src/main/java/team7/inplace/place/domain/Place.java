package team7.inplace.place.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity(name = "places")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    //@Column(columnDefinition = "json")
    private String facility;


    @Column(columnDefinition = "TEXT")
    private String menuImgUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Embedded
    private Address address;

    @Embedded
    private Coordinate coordinate;

    @ElementCollection
    private List<OffDay> offDays;

    @ElementCollection
    private List<OpenTime> openPeriods;

    @ElementCollection
    private List<Menu> menus;

    private LocalDateTime menuUpdatedAt;

    @ElementCollection
    private List<String> menuboardphotourlList;

    public Place(String name, String facility, String menuImgsUrl, String category,
                 String address, String x, String y,
                 List<String> offDays,
                 List<String> openPeriods,
                 List<String> menus,
                 LocalDateTime menuUpdatedAt,
                 List<String> menuboardphotourlList) {
        this.name = name;
        this.facility = facility;
        this.menuImgUrl = menuImgsUrl;
        this.category = Category.of(category);
        this.address = Address.of(address);
        this.coordinate = Coordinate.of(x, y);
        this.offDays = offDays.stream()
                .map(OffDay::of)
                .toList();
        this.openPeriods = openPeriods.stream()
                .map(OpenTime::of)
                .toList();
        this.menus = menus.stream()
                .map(Menu::of)
                .toList();
        this.menuUpdatedAt = menuUpdatedAt;
        this.menuboardphotourlList = menuboardphotourlList;
    }
}
