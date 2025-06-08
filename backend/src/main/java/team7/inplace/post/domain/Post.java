package team7.inplace.post.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import team7.inplace.global.baseEntity.BaseEntity;

@Entity
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    private PostTitle title;
    private PostContent content;
    private PostPhoto photos;
    private Long authorId;

    public Post(
        String title, String content,
        List<String> imageUrls, List<String> imgHashes,
        Long authorId
    ) {
        this.title = new PostTitle(title);
        this.content = new PostContent(content);
        this.photos = new PostPhoto(imageUrls, imgHashes);
        this.authorId = authorId;
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
        String title, String content,
        List<String> imageUrls, List<String> imgHashes
    ) {
        this.title = this.title.update(title);
        this.content = this.content.update(content);
        this.photos = new PostPhoto(imageUrls, imgHashes);
    }
}
