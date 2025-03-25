package team7.inplace.search.application.dto;

public enum SearchType {
    PLACE, INFLUENCER, VIDEO, ALL;

    public static SearchType from(String type) {
        if (type == null || type.isBlank()) {
            return ALL;
        }

        if (!type.equals("all") && !type.equals("place")) {
            throw new IllegalArgumentException("Invalid type");
        }

        return SearchType.valueOf(type.toUpperCase());
    }
}