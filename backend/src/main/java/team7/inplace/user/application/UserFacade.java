package team7.inplace.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team7.inplace.global.annotation.Facade;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;
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
import team7.inplace.video.application.VideoService;

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
        Long userId = AuthorizationUtil.getUserId()
            .orElseThrow(() -> InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY));
        return influencerService.getFavoriteInfluencers(userId, pageable);
    }

    //TODO: Return 클래스 변경 필요
    public Page<PlaceInfo.Simple> getMyFavoritePlaces(Pageable pageable) {
        Long userId = AuthorizationUtil.getUserId()
            .orElseThrow(() -> InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY));

        var likedPlaces = placeService.getLikedPlaceInfo(userId, pageable);
        var placeIds = likedPlaces.getContent()
            .stream()
            .map(PlaceQueryResult.DetailedPlace::placeId)
            .toList();

        var videoInfos = videoService.getVideosByPlaceId(placeIds);

        return likedPlaces.map(
            place -> PlaceInfo.Simple.of(place, videoInfos.get(place.placeId())));
    }

    public Page<ReviewQueryResult.Detail> getMyReviews(Pageable pageable) {
        Long userId = AuthorizationUtil.getUserId()
            .orElseThrow(() -> InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY));
        return reviewService.getUserReviews(userId, pageable);
    }

    public void updateNickname(String nickname) {
        Long userId = AuthorizationUtil.getUserId()
            .orElseThrow(() -> InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY));
        userService.updateNickname(userId, nickname);
    }

    public UserInfo getUserInfo() {
        Long userId = AuthorizationUtil.getUserId()
            .orElseThrow(() -> InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY));
        return userService.getUserInfo(userId);
    }

    public void deleteUser() {
        Long userId = AuthorizationUtil.getUserId()
            .orElseThrow(() -> InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY));
        oauthTokenService.unlinkOauthToken(userId);
        userService.deleteUser(userId);
    }
}
