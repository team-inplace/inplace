package team7.inplace.search.presentation.dto;

import lombok.Getter;
import lombok.ToString;
import team7.inplace.search.application.dto.SearchType;

public class SearchRequest {

    @Getter
    @ToString
    public static class AutoComplete {

        private final String value;
        private final SearchType type;

        public AutoComplete(String value, String type) {
            this.value = value;
            this.type = SearchType.from(type);
        }
    }

}
