package team7.inplace.liked.likedInfluencer.presentation.dto;

import team7.inplace.liked.likedInfluencer.application.dto.FavoriteInfluencerListCommand;

import java.util.List;

public record InfluencerListLikeRequest(
        List<Long> influencerIds,
        Boolean likes
) {

    public FavoriteInfluencerListCommand toCommand() {
        return new FavoriteInfluencerListCommand(influencerIds, likes);
    }
}
