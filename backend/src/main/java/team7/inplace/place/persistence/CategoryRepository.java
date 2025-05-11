package team7.inplace.place.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team7.inplace.place.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c.id FROM categories c WHERE c.name = :categoryName")
    Long findCategoryIdByName(String categoryName);

    @Query("select c.id from categories c where c.parentId is null")
    List<Long> findParentCategoryIds();
}
