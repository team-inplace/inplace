package my.inplace.domain.review;

import static lombok.AccessLevel.PROTECTED;

import my.inplace.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "reviews", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "place_id"})
})
public class Review extends BaseEntity {

    @Column(nullable = false)
    private Boolean isLiked;

    @Column(length = 100)
    private String comment;

    private LocalDate createdDate;

    private Long userId;

    private Long placeId;

    public Review(Long userId, Long placeId, boolean isLiked, String comment) {
        this.userId = userId;
        this.placeId = placeId;
        this.isLiked = isLiked;
        this.comment = comment;
        this.createdDate = LocalDate.now();
    }

    public Boolean isLiked() {
        return isLiked;
    }

    public Boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }

    public Boolean isNotOwner(Long userId) {
        return !isOwner(userId);
    }
}
