package my.inplace.application.influencer.command.dto;

import my.inplace.domain.influencer.Influencer;
import my.inplace.domain.influencer.LikedInfluencer;
import java.util.List;

public class InfluencerCommand {

    public record InfluencerCreate(
        String influencerName,
        String influencerImgUrl,
        String influencerJob,
        String channelTitle,
        String channelId
    ) {

        public Influencer toEntity() {
            return new Influencer(
                influencerName(),
                influencerImgUrl(),
                influencerJob(),
                channelTitle(),
                channelId()
            );
        }
    }

    public record InfluencerUpdate(
        Long id,
        String influencerName,
        String influencerImgUrl,
        String influencerJob,
        String channelTitle,
        String channelId
    ) {

    }

    public record LikeSingle(
        Long influencerId,
        Boolean like
    ) {

        public LikedInfluencer toEntity(Long userId) {
            return new LikedInfluencer(userId, influencerId, like);
        }
    }

    public record LikeMultiple(
        List<LikeSingle> likes
    ) {

    }
}
