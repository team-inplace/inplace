package team7.inplace.place.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {
    private String price;

    @ColumnDefault("false")
    @Column(nullable = false)
    private boolean recommend;

    @Column(length = 50, nullable = false)
    private String menuName;

    private String menuImgUrl;

    private String description;

    private Menu(String price, boolean recommend, String menuName, String menuImgUrl, String description) {
        this.price = price;
        this.recommend = recommend;
        this.menuName = menuName;
        this.menuImgUrl = menuImgUrl;
        this.description = description;
    }

    public static Menu of(String menu) {
        String[] menus = menu.split("\\|");
        return new Menu(menus[1], Boolean.parseBoolean(menus[2]), menus[0], menus[3], menus[4]);
    }

}
