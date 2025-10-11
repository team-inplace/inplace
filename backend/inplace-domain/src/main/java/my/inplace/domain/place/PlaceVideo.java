package my.inplace.domain.place;


import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "place_videos")
@NoArgsConstructor(access = PROTECTED)
public class PlaceVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long placeId;
    private Long videoId;

    public PlaceVideo(Long placeId, Long videoId) {
        this.placeId = placeId;
        this.videoId = videoId;
    }
}
