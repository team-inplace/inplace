package my.inplace.domain.post;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.PostErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostContent {

    private String content;

    public PostContent(String content) {
        validateContent(content);
        this.content = content;
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw InplaceException.of(PostErrorCode.POST_CONTENT_EMPTY);
        }

        if (content.length() > 3000) {
            throw InplaceException.of(PostErrorCode.POST_CONTENT_LENGTH_EXCEEDED);
        }
    }

    public PostContent update(String content) {
        return new PostContent(content);
    }
}
