package place.query;

import annotation.Facade;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import place.query.PlaceQueryResult.MarkerDetail;
import place.query.dto.PlaceParam;
import place.query.dto.PlaceResult;
import util.AuthorizationUtil;
import video.query.VideoQueryService;

@Facade
@RequiredArgsConstructor
public class PlaceQueryFacade {

    private final PlaceQueryService placeQueryService;
    private final VideoQueryService videoQueryService;
    private final Executor externalApiExecutor;

    public PlaceResult.Detail getDetailedPlaces(
        Long placeId
    ) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var googlePlaceId = placeQueryService.getGooglePlaceId(placeId);
        if (googlePlaceId.isEmpty()) {
            var placeInfo = placeQueryService.getPlaceInfo(userId, placeId);

            var videoSearchParam = VideoSearchParams.from(
                placeInfo.longitude().toString(),
                placeInfo.latitude().toString()
            );
            var surroundVideos = videoQueryService.getVideosBySurround(
                videoSearchParam,
                PageRequest.of(0, 10),
                false
            );
            var videoInfos = videoQueryService.getVideosByPlaceId(placeId);
            var reviewRates = reviewQueryService.getReviewLikeRate(placeId);
            return PlaceResult.Detail.of(placeInfo, null, videoInfos, reviewRates, surroundVideos);
        }

        var googlePlace = CompletableFuture.supplyAsync(
            () -> placeQueryService.getGooglePlaceInfo(googlePlaceId.get()),
            externalApiExecutor
        ).exceptionally(e -> null);

        var placeInfo = placeQueryService.getPlaceInfo(userId, placeId);
        var surroundVideos = videoQueryService.getVideosBySurround(
            VideoSearchParams.from(placeInfo.longitude().toString(),
                placeInfo.latitude().toString()),
            PageRequest.of(0, 10),
            false
        );
        var videoInfos = videoQueryService.getVideosByPlaceId(placeId);
        var reviewRates = reviewQueryService.getReviewLikeRate(placeId);

        return PlaceResult.Detail.of(placeInfo, googlePlace.join(), videoInfos, reviewRates,
            surroundVideos);
    }

    public Page<PlaceResult.Simple> getPlacesInMapRange(
        PlaceParam.Coordinate coordinate,
        PlaceParam.Filter filterParams,
        Pageable pageable
    ) {
        var userId = AuthorizationUtil.getUserIdOrNull();

        var placeSimpleInfos = placeQueryService.getPlacesInMapRange(
            userId,
            coordinate,
            filterParams,
            pageable
        );
        var placeIds = placeSimpleInfos.getContent()
            .stream()
            .map(PlaceQueryResult.DetailedPlace::placeId)
            .toList();
        var placeVideos = videoQueryService.getVideosByPlaceId(placeIds);

        return placeSimpleInfos.map(
            place -> PlaceResult.Simple.of(place, placeVideos.get(place.placeId()))
        );
    }

    public Page<PlaceResult.Simple> getPlacesByName(
        String name,
        PlaceParam.Filter filterParams,
        Pageable pageable
    ) {
        var userId = AuthorizationUtil.getUserIdOrNull();

        var placeSimpleInfos = placeQueryService.getPlacesByName(userId, name, filterParams, pageable);
        var placeIds = placeSimpleInfos.getContent()
            .stream()
            .map(PlaceQueryResult.DetailedPlace::placeId)
            .toList();
        var placeVideos = videoQueryService.getVideosByPlaceId(placeIds);

        return placeSimpleInfos.map(
            place -> PlaceResult.Simple.of(place, placeVideos.get(place.placeId()))
        );
    }

    /*
    * Category 관련
     */

    public List<PlaceResult.Category> getCategories() {
        return placeQueryService.getCategories();
    }

    public List<PlaceResult.Category> getSubCategoriesByParentId(Long parentCategoryId) {
        return placeQueryService.getSubCategoriesByParentId(parentCategoryId);
    }

    // TODO - 어드민 전용 Place 조회 이므로 변경 필요
    public List<PlaceResult.Simple> getAdminPlacesByVideoId(Long videoId) {
        return placeQueryService.getSimplePlacesByVideoId(videoId)
            .stream()
            .map((place) -> PlaceResult.Simple.of(place, null))
            .toList();
    }

    /*
     * Marker 관련 매서드
     */
    public PlaceResult.Marker getMarkerInfo(
        Long placeId
    ) {
        MarkerDetail markerDetail = placeQueryService.getMarkerInfo(placeId);
        var videos = videoQueryService.getVideosByPlaceId(placeId);
        return PlaceResult.Marker.of(markerDetail, videos);
    }

    public List<PlaceResult.Marker> getPlaceLocations(
        PlaceParam.Coordinate coordinateParams,
        PlaceParam.Filter filterParams
    ) {
        return placeQueryService.getPlaceLocations(coordinateParams, filterParams);
    }

    public List<PlaceResult.Marker> getPlaceLocationsByName(
        String name,
        PlaceParam.Filter filterParams
    ) {
        return placeQueryService.getPlaceLocationsByName(name, filterParams);
    }
}
