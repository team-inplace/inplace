package team7.inplace.place.application.dto;


import team7.inplace.liked.likedPlace.domain.LikedPlace;

public record LikedPlaceInfo(
        Long placeId,
        String placeName,
        String imageUrl,
        String influencerName,
        boolean likes
) {

    public static LikedPlaceInfo of(LikedPlace likedPlace, String influencerName) {
        return new LikedPlaceInfo(
                likedPlace.getPlace().getId(),
                likedPlace.getPlace().getName(),
                likedPlace.getPlace().getMenuImgUrl(),
                influencerName,
                likedPlace.isLiked()
        );
    }
}
