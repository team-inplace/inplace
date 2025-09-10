package my.inplace.api.influencer.dto;

import my.inplace.application.influencer.command.dto.InfluencerCommand.InfluencerCreate;
import my.inplace.application.influencer.command.dto.InfluencerCommand.InfluencerUpdate;
import my.inplace.application.influencer.command.dto.InfluencerCommand.LikeMultiple;
import my.inplace.application.influencer.command.dto.InfluencerCommand.LikeSingle;
import java.util.List;

public class InfluencerRequest {

    public record Upsert(
        String influencerName,
        String influencerImgUrl,
        String influencerJob,
        String channelTitle,
        String channelId
    ) {

        public InfluencerCreate toCommand() {
            return new InfluencerCreate(
                influencerName(),
                influencerImgUrl(),
                influencerJob(),
                channelTitle(),
                channelId()
            );
        }

        public InfluencerUpdate toCommand(Long id) {
            return new InfluencerUpdate(
                id,
                influencerName(),
                influencerImgUrl(),
                influencerJob(),
                channelTitle(),
                channelId()
            );
        }
    }

    public record Like(
        Long influencerId,
        Boolean likes
    ) {

        public LikeSingle toCommand() {
            return new LikeSingle(influencerId(), likes());
        }
    }

    public record Likes(
        List<Long> influencerIds,
        Boolean likes
    ) {

        public LikeMultiple toCommand() {
            var command = influencerIds.stream()
                .map(influencerId -> new LikeSingle(influencerId, likes))
                .toList();

            return new LikeMultiple(command);
        }
    }
}
