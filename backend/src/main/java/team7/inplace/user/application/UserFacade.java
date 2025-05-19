package team7.inplace.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team7.inplace.global.annotation.Facade;
import team7.inplace.influencer.application.InfluencerService;
import team7.inplace.influencer.application.dto.InfluencerInfo;
import team7.inplace.place.application.PlaceService;
import team7.inplace.place.application.dto.PlaceInfo;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.review.application.ReviewService;
import team7.inplace.review.persistence.dto.ReviewQueryResult;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.token.application.OauthTokenService;
import team7.inplace.user.application.dto.UserInfo;
import team7.inplace.user.presentation.dto.UserResponse;
import team7.inplace.video.application.VideoService;

import java.util.Map;
import java.util.stream.Collectors;

@Facade
@RequiredArgsConstructor
public class UserFacade {

    private final InfluencerService influencerService;
    private final PlaceService placeService;
    private final ReviewService reviewService;
    private final VideoService videoService;
    private final UserService userService;
    private final OauthTokenService oauthTokenService;

    //TODO: Return 클래스 변경 필요
    public Page<InfluencerInfo> getMyFavoriteInfluencers(Pageable pageable) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        return influencerService.getFavoriteInfluencers(userId, pageable);
    }

    //TODO: Return 클래스 변경 필요
    public Page<PlaceInfo.Simple> getMyFavoritePlaces(Pageable pageable) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();

        var likedPlaces = placeService.getLikedPlaceInfo(userId, pageable);
        var placeIds = likedPlaces.getContent()
            .stream()
            .map(PlaceQueryResult.DetailedPlace::placeId)
            .toList();

        var videoInfos = videoService.getVideosByPlaceId(placeIds);

        return likedPlaces.map(
            place -> PlaceInfo.Simple.of(place, videoInfos.get(place.placeId())));
    }

    public Page<UserInfo.Review> getMyReviews(Pageable pageable) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        var details = reviewService.getUserReviews(userId, pageable);
        var placeIds = details.stream().map(ReviewQueryResult.Detail::placeId).toList();
        var videoUrls = videoService.getVideosByPlaceId(placeIds).entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().get(0).videoUrl())
            );

        return details
                .map((detail) -> UserInfo.Review.from(detail, videoUrls.get(detail.placeId())));
    }

    public void updateNickname(String nickname) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        userService.updateNickname(userId, nickname);
    }

    public UserInfo.Profile getUserInfo() {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        return userService.getUserInfo(userId);
    }

    public void deleteUser() {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        oauthTokenService.unlinkOauthToken(userId);
        userService.deleteUser(userId);
    }
}
