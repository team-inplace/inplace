package team7.inplace.liked.likedPost.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import team7.inplace.global.baseEntity.BaseEntity;

@Entity
@Table(name = "liked_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikedPost extends BaseEntity {

    private Long userId;
    private Long postId;

    public LikedPost(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
