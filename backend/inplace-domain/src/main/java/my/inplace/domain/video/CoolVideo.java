package my.inplace.domain.video;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cool_videos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoolVideo {

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

    @Column(name = "place_category_parent_name")
    private String placeCategoryParentName;

    @Column(name = "place_category_parent_id")
    private Long placeCategoryParentId;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "address3")
    private String address3;

    private CoolVideo(
        Long videoId,
        String videoUUID,
        String influencerName,
        Long placeId,
        String placeName,
        String placeCategoryParentName,
        Long placeCategoryParentId,
        String address1,
        String address2,
        String address3
    ) {
        this.videoId = videoId;
        this.videoUUID = videoUUID;
        this.influencerName = influencerName;
        this.placeId = placeId;
        this.placeName = placeName;
        this.placeCategoryParentName = placeCategoryParentName;
        this.placeCategoryParentId = placeCategoryParentId;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
    }

    public static CoolVideo from(
        Long videoId,
        String videoUUID,
        String influencerName,
        Long placeId,
        String placeName,
        String placeCategoryParentName,
        Long placeCategoryParentId,
        String address1,
        String address2,
        String address3
    ) {
        return new CoolVideo(
            videoId,
            videoUUID,
            influencerName,
            placeId,
            placeName,
            placeCategoryParentName,
            placeCategoryParentId,
            address1,
            address2,
            address3
        );
    }

    public String getVideoUrl() {
        return "https://www.youtube.com/watch?v=" + videoUUID;
    }
}
