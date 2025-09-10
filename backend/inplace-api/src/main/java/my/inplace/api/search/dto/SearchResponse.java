package my.inplace.api.search.dto;

import my.inplace.application.search.query.dto.SearchResult;
import my.inplace.application.search.query.dto.SearchType;

public class SearchResponse {

    public record AutoCompletion (
        String result,
        Double score,
        SearchType searchType
    ) {
        public static AutoCompletion from(SearchResult.AutoCompletion completion) {
            return new AutoCompletion(
                completion.result(),
                completion.score(),
                completion.searchType()
            );
        }
    }

    public record Place(
        Long placeId,
        String placeName,
        String placeImgUrl,
        boolean likes
    ) {

        public static Place from(SearchResult.Place placeResult) {
            return new Place(
                placeResult.id(),
                placeResult.name(),
                "",
                placeResult.likes()
            );
        }

    }
}
