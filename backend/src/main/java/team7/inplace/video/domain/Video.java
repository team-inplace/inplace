package team7.inplace.video.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team7.inplace.global.baseEntity.BaseEntity;

@Getter
@Entity
@Table(name = "videos")
@NoArgsConstructor(access = PROTECTED)
public class Video extends BaseEntity {
    private String uuid;

    @Embedded
    private View view;

    private LocalDateTime publishTime;

    private Long influencerId;

    private Long placeId;

    public Video(String uuid, LocalDateTime publishTime, Long influencerId) {
        this.uuid = uuid;
        this.view = new View();
        this.publishTime = publishTime;
        this.influencerId = influencerId;
    }

    public static Video from(String uuid, LocalDateTime publishTime, Long influencerId) {
        return new Video(uuid, publishTime, influencerId);
    }

    public String getVideoUrl() {
        return String.format("https://www.youtube.com/watch?v=%s", uuid);
    }

    public Long getViewCount() {
        return view.getViewCount();
    }

    public Long getViewCountIncrease() {
        return view.getViewCountIncrease();
    }

    public void updateViewCount(Long viewCount) {
        view.updateViewCount(viewCount);
    }

    public void addPlace(Long placeId) {
        this.placeId = placeId;
    }

}
