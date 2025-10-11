package my.inplace.domain.influencer;

import static lombok.AccessLevel.PROTECTED;

import my.inplace.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "influencers")
@NoArgsConstructor(access = PROTECTED)
public class Influencer extends BaseEntity {

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 20)
    private String job;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imgUrl;

    @Embedded
    private Channel channel;

    private Boolean hidden;

    public Influencer(String name, String imgUrl, String job, String title, String channelId) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.job = job;
        this.channel = new Channel(title, channelId);
        this.hidden = false;
    }

    public void update(String name, String imgUrl, String job) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.job = job;
    }

    public String getChannelTitle() {
        return this.channel.getChannelTitle();
    }

    public String getChannelId() {
        return this.channel.getChannelId();
    }

    public String getLastMediumVideo() {
        return this.channel.getLastMediumVideoId();
    }

    public String getLastLongVideo() {
        return this.channel.getLastLongVideoId();
    }

    public void changeVisibility() {
        this.hidden = !this.hidden;
    }

    public void updateLastMediumVideo(String lastMediumVideo) {
        this.channel.updateLastMediumVideo(lastMediumVideo);
    }

    public void updateLastLongVideo(String lastLongVideoId) {
        this.channel.updateLastLongVideo(lastLongVideoId);
    }
}
