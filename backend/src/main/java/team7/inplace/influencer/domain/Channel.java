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

    public Channel(String channelTitle, String channelId) {
        this.channelTitle = channelTitle;
        this.channelId = channelId;
        this.lastVideoId = null;
    }

    public void updateLastVideo(String lastVideoId) {
        this.lastVideoId = lastVideoId;
    }
}
