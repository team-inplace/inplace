package team7.inplace.admin.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team7.inplace.place.domain.Category;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter @Setter
public class CategoryForm {

    private Long id;
    private String name;
    private String engName;
    private Long parentId;

    public static CategoryForm of(Category category) {
        return new CategoryForm(
            category.getId(),
            category.getName(),
            category.getEngName(),
            category.getParentId()
        );
    }

    public Category toEntity() {
        return new Category(
            name,
            engName,
            parentId
        );
    }
}
