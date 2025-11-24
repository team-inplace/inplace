package my.inplace.application.user;

import java.util.List;
import my.inplace.application.annotation.Facade;
import my.inplace.application.influencer.query.InfluencerQueryService;
import my.inplace.application.influencer.query.dto.InfluencerResult;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import my.inplace.application.post.query.PostQueryService;
import my.inplace.application.post.query.dto.PostResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import my.inplace.domain.place.query.PlaceQueryResult;
import my.inplace.application.place.query.PlaceQueryService;
import my.inplace.application.place.query.dto.PlaceResult;
import my.inplace.application.review.ReviewService;
import my.inplace.domain.review.query.ReviewQueryResult;
import my.inplace.application.user.command.UserCommandService;
import my.inplace.application.user.dto.UserResult;
import my.inplace.application.user.query.UserQueryService;
import my.inplace.security.util.AuthorizationUtil;
import my.inplace.application.video.query.VideoQueryService;

@Facade
@RequiredArgsConstructor
public class UserFacade {

    private final InfluencerQueryService influencerQueryService;
    private final PlaceQueryService placeService;
    private final ReviewService reviewService;
    private final VideoQueryService videoQueryService;
    private final UserQueryService userQueryService;
    private final PostQueryService postQueryService;

    private final UserCommandService userCommandService;

    //TODO: Return 클래스 변경 필요
    public Page<InfluencerResult.Simple> getMyFavoriteInfluencers(Pageable pageable) {
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
        userCommandService.deleteUser(userId);
    }

    public UserResult.Info getUserInfo() {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        return userQueryService.getUserInfo(userId);
    }

    public List<UserResult.BadgeWithOwnerShip> getAllBadges() {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        return userQueryService.getAllBadgesWithOwnerShip(userId);
    }

    public void updateMainBadge(Long badgeId) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        userCommandService.updateBadge(userId, badgeId);
    }
    
    public void updateReportResent(Boolean isResented) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        
        userCommandService.updateReportPushResent(userId, isResented);
    }
    
    public void updateMentionResent(Boolean isResented) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        
        userCommandService.updateMentionPushResent(userId, isResented);
    }
    
    public Page<PostResult.DetailedPost> getMyPosts(Pageable pageable) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        UserResult.Info userInfo = userQueryService.getUserInfo(userId);
        
        return postQueryService.getMyPosts(userId, pageable)
            .map(simplePost -> PostResult.DetailedPost.of(simplePost, userInfo));
    }
    
    public Page<PostResult.DetailedPost> getMyLikedPosts(Pageable pageable) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        UserResult.Info userInfo = userQueryService.getUserInfo(userId);
        
        return postQueryService.getLikedPosts(userId, pageable)
            .map(simplePost -> PostResult.DetailedPost.of(simplePost, userInfo));
    }
    
    public Page<PostResult.DetailedPost> getMyCommentedPosts(Pageable pageable) {
        Long userId = AuthorizationUtil.getUserIdOrThrow();
        UserResult.Info userInfo = userQueryService.getUserInfo(userId);
        
        return postQueryService.getCommentedPosts(userId, pageable)
            .map(simplePost -> PostResult.DetailedPost.of(simplePost, userInfo));
    }
}
