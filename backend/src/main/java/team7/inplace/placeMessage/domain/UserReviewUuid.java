package team7.inplace.placeMessage.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team7.inplace.user.domain.User;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class UserReviewUuid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long placeId;

    @Column(nullable = false, unique = true, length = 255)
    private String uuid;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    public UserReviewUuid(User user, Long placeId, String uuid) {
        this.user = user;
        this.placeId = placeId;
        this.uuid = uuid;
        this.createdDate = LocalDateTime.now();
    }
}
