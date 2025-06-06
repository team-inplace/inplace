package team7.inplace.post.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import team7.inplace.global.baseEntity.BaseEntity;

@Entity
@Table(name = "post_photos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPhoto extends BaseEntity {

    private Long postId;
    private String photoUrl;

    public PostPhoto(Long postId, String photoUrl) {
        this.postId = postId;
        this.photoUrl = photoUrl;
    }
}
