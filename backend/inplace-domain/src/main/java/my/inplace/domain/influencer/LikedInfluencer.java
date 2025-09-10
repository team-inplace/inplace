package my.inplace.domain.influencer;

import static lombok.AccessLevel.PROTECTED;

import my.inplace.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "liked_influencers")
@NoArgsConstructor(access = PROTECTED)
public class LikedInfluencer extends BaseEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long influencerId;

    @Column(nullable = false)
    private boolean isLiked = false;

    public LikedInfluencer(Long userId, Long influencerId, boolean isLiked) {
        this.userId = userId;
        this.influencerId = influencerId;
        this.isLiked = isLiked;
    }

    public void like() {
        this.isLiked = true;
    }

    public void unlike() {
        this.isLiked = false;
    }
}
