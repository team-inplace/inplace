package my.inplace.domain.place;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "categories")
@NoArgsConstructor(access = PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String engName;

    private Long parentId;

    public Category(String name, String engName, Long parentId) {
        this.name = name;
        this.engName = engName;
        this.parentId = parentId;
    }

    public void updateInfo(String name, String engName, Long parentId) {
        this.name = name;
        this.engName = engName;
        this.parentId = parentId;
    }
}
