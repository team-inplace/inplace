package team7.inplace.place.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import team7.inplace.place.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
