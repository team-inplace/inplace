package my.inplace.domain.post;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.PostErrorCode;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
