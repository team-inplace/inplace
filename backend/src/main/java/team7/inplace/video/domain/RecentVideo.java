package team7.inplace.video.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team7.inplace.place.domain.Category;

@Getter
@Entity
@Table(name = "recent_videos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecentVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "video_id")
    private Long videoId;

    @Column(name = "uuid")
    private String videoUUID;

    @Column(name = "influencer_name")
    private String influencerName;

    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "place_category")
    @Enumerated(value = EnumType.STRING)
    private Category placeCategory;

    private RecentVideo(Long videoId, String videoUUID, String influencerName, Long placeId, String placeName,
        Category placeCategory) {
        this.videoId = videoId;
        this.videoUUID = videoUUID;
        this.influencerName = influencerName;
        this.placeId = placeId;
        this.placeName = placeName;
        this.placeCategory = placeCategory;
    }

    public static RecentVideo from(Long videoId, String videoUUID, String influencerName, Long placeId, String placeName, Category placeCategory) {
        return new RecentVideo(videoId, videoUUID, influencerName, placeId, placeName, placeCategory);
    }

}
