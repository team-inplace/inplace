package team7.inplace.place.domain;

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

    public Category(String name, Long parentId) {
        this.name = name;
        this.parentId = parentId;
    }

}
