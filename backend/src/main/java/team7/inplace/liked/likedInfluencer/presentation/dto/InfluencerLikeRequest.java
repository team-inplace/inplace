package team7.inplace.liked.likedInfluencer.presentation.dto;

import team7.inplace.liked.likedInfluencer.application.dto.FavoriteInfluencerCommand;

public record InfluencerLikeRequest(
        Long influencerId,
        Boolean likes
) {

    public FavoriteInfluencerCommand toCommand() {
        return new FavoriteInfluencerCommand(influencerId, likes);
    }
}
