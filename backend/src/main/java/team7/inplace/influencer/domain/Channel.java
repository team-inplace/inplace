package team7.inplace.influencer.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Channel {

    private String channelTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String channelId;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String lastVideoId;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String lastLongVideoId;

    public Channel(String channelTitle, String channelId) {
        this.channelTitle = channelTitle;
        this.channelId = channelId;
        this.lastVideoId = null;
    }

    public void updateLastMediumVideo(String lastVideoId) {
        this.lastVideoId = lastVideoId;
    }

    public void updateLastLongVideo(String lastLongVideoId) {
        this.lastLongVideoId = lastLongVideoId;
    }
}
