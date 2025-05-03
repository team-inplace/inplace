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

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "address3")
    private String address3;

    private RecentVideo(
        Long videoId,
        String videoUUID,
        String influencerName,
        Long placeId,
        String placeName,
        Category placeCategory,
        String address1,
        String address2,
        String address3
    ) {
        this.videoId = videoId;
        this.videoUUID = videoUUID;
        this.influencerName = influencerName;
        this.placeId = placeId;
        this.placeName = placeName;
        this.placeCategory = placeCategory;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
    }

    public static RecentVideo from(
        Long videoId,
        String videoUUID,
        String influencerName,
        Long placeId,
        String placeName,
        Category placeCategory,
        String address1,
        String address2,
        String address3
    ) {
        return new RecentVideo(
            videoId,
            videoUUID,
            influencerName,
            placeId,
            placeName,
            placeCategory,
            address1,
            address2,
            address3
        );
    }

}
