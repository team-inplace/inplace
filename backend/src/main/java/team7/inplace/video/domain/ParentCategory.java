package team7.inplace.video.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.VideoErrorCode;

@Getter
@RequiredArgsConstructor
public enum ParentCategory {
    EATS("맛집"),
    PLAYS("놀거리");

    private final String parentCategory;

    public static ParentCategory from(String categoryName) {
        for (ParentCategory category : values()) {
            if (category.name().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }
        throw InplaceException.of(VideoErrorCode.INVALID_CATEGORY);
    }

}
