package team7.inplace.post.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.PostErrorCode;

@Getter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PostTitle {

    private String title;

    public PostTitle(String title) {
        validateTitle(title);
        this.title = title;
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw InplaceException.of(PostErrorCode.POST_TITLE_EMPTY);
        }
        if (title.length() > 30) {
            throw InplaceException.of(PostErrorCode.POST_TITLE_LENGTH_EXCEEDED);
        }
    }

    public PostTitle update(String title) {
        return new PostTitle(title);
    }
}
