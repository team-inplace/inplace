package my.inplace.domain.post;

import lombok.Getter;
import my.inplace.domain.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "liked_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikedPost extends BaseEntity {

    private Long userId;
    private Long postId;
    private Boolean isLiked;

    private LikedPost(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
        this.isLiked = false;
    }

    public static LikedPost from(Long userId, Long postId) {
        return new LikedPost(userId, postId);
    }

    public boolean updateLike(Boolean like) {
        return this.isLiked = like;
    }
}
