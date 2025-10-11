package my.inplace.domain.search;

public class SearchQueryResult {

    public record AutoComplete(
        String keyword,
        String type,
        Double score
    ) {

    }

    public record Place(
        Long id,
        String name,
        boolean likes
    ) {

    }
}
