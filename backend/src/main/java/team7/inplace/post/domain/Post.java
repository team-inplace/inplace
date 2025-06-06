package team7.inplace.post.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import team7.inplace.global.baseEntity.BaseEntity;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.PostErrorCode;

@Entity
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    private String title;
    private String content;
    private Long authorId;

    public Post(String title, String content, Long authorId) {
        validateTitle(title);
        validateContent(content);
        this.title = title;
        this.content = content;
        this.authorId = authorId;
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw InplaceException.of(PostErrorCode.POST_TITLE_EMPTY);
        }
        if (title.length() > 30) {
            throw InplaceException.of(PostErrorCode.POST_TITLE_LENGTH_EXCEEDED);
        }
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw InplaceException.of(PostErrorCode.POST_CONTENT_EMPTY);
        }

        if (content.length() > 3000) {
            throw InplaceException.of(PostErrorCode.POST_CONTENT_EMPTY);
        }
    }
}
