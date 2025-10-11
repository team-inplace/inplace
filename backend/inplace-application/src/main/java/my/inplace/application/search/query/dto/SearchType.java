package my.inplace.application.search.query.dto;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.SearchErrorCode;

public enum SearchType {
    PLACE, INFLUENCER, VIDEO, ALL;

    public static SearchType from(String type) {
        if (type == null || type.isBlank()) {
            return ALL;
        }

        if (!type.equals("all") && !type.equals("place")) {
            throw InplaceException.of(SearchErrorCode.SEARCH_TYPE_INVALID);
        }

        return SearchType.valueOf(type.toUpperCase());
    }
}
