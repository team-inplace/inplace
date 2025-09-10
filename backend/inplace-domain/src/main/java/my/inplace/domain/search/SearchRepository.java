package my.inplace.domain.search;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchRepository<D> {

    Integer SEARCH_LIMIT = 10;

    default List<SearchQueryResult.AutoComplete> searchAutoComplete(String keyword) {
        return List.of();
    }

    Page<D> search(String keyword, Pageable pageable, Long userId);
}
