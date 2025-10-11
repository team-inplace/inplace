package my.inplace.application.search.query.dto;

import my.inplace.domain.search.SearchQueryResult;

public class SearchResult {

    public record AutoCompletion(
        String result,
        Double score,
        SearchType searchType
    ) {

    }

    public record Place(
        Long id,
        String name,
        boolean likes
    ) {

        public static Place from(SearchQueryResult.Place place) {
            return new Place(
                place.id(),
                place.name(),
                place.likes()
            );
        }

    }
}