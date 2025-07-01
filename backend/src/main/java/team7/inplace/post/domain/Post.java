package team7.inplace.post.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team7.inplace.global.baseEntity.BaseEntity;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.PostErrorCode;

@Getter
@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Embedded
    private PostTitle title;
    @Embedded
    private PostContent content;
    @Embedded
    private PostPhoto photos;
    private Integer totalLikeCount;
    private Integer totalCommentCount;
    private Long authorId;
    private Boolean isReported = false;

    public Post(
        String title, String content,
        List<String> imageUrls, List<String> imgHashes,
        Long authorId
    ) {
        this.title = new PostTitle(title);
        this.content = new PostContent(content);
        this.photos = new PostPhoto(imageUrls, imgHashes);
        this.authorId = authorId;
        this.totalLikeCount = 0;
        this.totalCommentCount = 0;
    }

    public String getTitle() {
        return title.getTitle();
    }

    public String getContent() {
        return content.getContent();
    }

    public List<String> getImageUrls() {
        return photos.getImageUrls();
    }

    public List<String> getImgHashes() {
        return photos.getImgHashes();
    }

    public void update(
        Long userId,
        String title,
        String content,
        List<String> imageUrls,
        List<String> imgHashes
    ) {
        validateAuthor(userId);
        this.title = this.title.update(title);
        this.content = this.content.update(content);
        this.photos = new PostPhoto(imageUrls, imgHashes);
    }

    public void deleteSoftly(Long userId) {
        validateAuthor(userId);
        super.deleteSoftly();
    }

    private void validateAuthor(Long userId) {
        if (this.authorId.equals(userId)) {
            return;
        }
        throw InplaceException.of(PostErrorCode.POST_CAN_NOT_BE_MODIFIED);
    }

    public void report() {
        if (!Boolean.TRUE.equals(this.isReported)) {
            this.isReported = true;
        }
    }
}
