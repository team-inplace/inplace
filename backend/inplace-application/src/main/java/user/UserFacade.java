package user;

import annotation.Facade;
import influencer.query.InfluencerQueryService;
import influencer.query.dto.InfluencerResult;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import place.PlaceService;
import place.dto.PlaceResult;
import place.query.PlaceQueryResult;
import review.ReviewService;
import review.query.ReviewQueryResult;
import token.OauthTokenService;
import user.command.UserCommandService;
import user.dto.UserResult;
import user.query.UserQueryService;
import util.AuthorizationUtil;
import video.query.VideoQueryService;

@Facade
@RequiredArgsConstructor
public class UserFacade {

    private final InfluencerQueryService influencerQueryService;
    private final PlaceService placeService;
    private final ReviewService reviewService;
    private final VideoQueryService videoQueryService;
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;
    private final OauthTokenService oauthTokenService;

    //TODO: Return 클래스 변경 필요
    public Page<InfluencerResult.Detail> getMyFavoriteInfluencers(Pageable pageable) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        return influencerQueryService.getFavoriteInfluencers(userId, pageable);
    }

    //TODO: Return 클래스 변경 필요
    public Page<PlaceResult.Simple> getMyFavoritePlaces(Pageable pageable) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();

        var likedPlaces = placeService.getLikedPlaceInfo(userId, pageable);
        var placeIds = likedPlaces.getContent()
            .stream()
            .map(PlaceQueryResult.DetailedPlace::placeId)
            .toList();

        var videoInfos = videoQueryService.getVideosByPlaceId(placeIds);

        return likedPlaces.map(
            place -> PlaceResult.Simple.of(place, videoInfos.get(place.placeId())));
    }

    public Page<UserResult.Review> getMyReviews(Pageable pageable) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        var details = reviewService.getUserReviews(userId, pageable);
        var placeIds = details.stream().map(ReviewQueryResult.Detail::placeId).toList();
        var videoUrls = videoQueryService.getVideosByPlaceId(placeIds).entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().get(0).videoUrl())
            );

        return details
            .map((detail) -> UserResult.Review.from(detail, videoUrls.get(detail.placeId())));
    }

    public void updateNickname(String nickname) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        userCommandService.updateNickname(userId, nickname);
    }

    public void deleteUser() {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        oauthTokenService.unlinkOauthToken(userId);
        userCommandService.deleteUser(userId);
    }

    public UserResult.Detail getUserDetail() {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        return userQueryService.getUserDetail(userId);
    }

    public void updateMainBadge(Long badgeId) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        userCommandService.updateBadge(userId, badgeId);
    }
}
