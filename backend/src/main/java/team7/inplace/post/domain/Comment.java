package team7.inplace.post.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import team7.inplace.global.baseEntity.BaseEntity;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.PostErrorCode;

@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    private Long postId;
    private Long authorId;
    private String content;

    public Comment(Long postId, Long authorId, String content) {
        validateContent(content);
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw InplaceException.of(PostErrorCode.COMMENT_CONTENT_EMPTY);
        }

        if (content.length() > 1000) {
            throw InplaceException.of(PostErrorCode.COMMENT_LENGTH_EXCEEDED);
        }
    }

    public void updateContent(Long authorId, String content) {
        validateAuthor(authorId);
        validateContent(content);
        this.content = content;
    }

    public void deleteSoftly(Long authorId) {
        validateAuthor(authorId);
        super.deleteSoftly();
    }

    private void validateAuthor(Long authorId) {
        if (this.authorId.equals(authorId)) {
            return;
        }
        throw InplaceException.of(PostErrorCode.COMMENT_CAN_NOT_BE_MODIFIED);
    }

}
