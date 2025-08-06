package influencer.dto;

import influencer.Influencer;

public record InfluencerResult(
    Long influencerId,
    String influencerName,
    String influencerImgUrl,
    String influencerJob,
    boolean likes
) {

    public static InfluencerResult from(Influencer influencer, boolean isLiked) {
        return new InfluencerResult(
            influencer.getId(),
            influencer.getName(),
            influencer.getImgUrl(),
            influencer.getJob(),
            isLiked
        );
    }
}
