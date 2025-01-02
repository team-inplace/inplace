package team7.inplace.liked.likedInfluencer.application.dto;

public record FavoriteInfluencerCommand(
        Long influencerId,
        Boolean likes
) {

}
