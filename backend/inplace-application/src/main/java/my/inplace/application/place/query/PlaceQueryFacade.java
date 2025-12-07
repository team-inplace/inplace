package my.inplace.application.place.query;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import my.inplace.application.annotation.Facade;
import my.inplace.application.place.query.dto.PlaceParam;
import my.inplace.application.place.query.dto.PlaceResult;
import my.inplace.application.place.query.dto.PlaceResult.Region;
import my.inplace.application.place.query.dto.PlaceResult.Simple;
import my.inplace.application.review.ReviewService;
import my.inplace.application.video.query.VideoQueryService;
import my.inplace.application.video.query.dto.VideoParam.SquareBound;
import my.inplace.common.cursor.CursorResult;
import my.inplace.domain.place.query.PlaceQueryResult;
import my.inplace.domain.place.query.PlaceQueryResult.MarkerDetail;
import my.inplace.security.util.AuthorizationUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Facade
public class PlaceQueryFacade {

    private final PlaceQueryService placeQueryService;
    private final ReviewService reviewQueryService;
    private final VideoQueryService videoQueryService;
    private final Executor externalApiExecutor;

    public PlaceQueryFacade(
        PlaceQueryService placeQueryService,
        ReviewService reviewQueryService,
        VideoQueryService videoQueryService,
        @Qualifier("externalApiExecutor") Executor externalApiExecutor
    ) {
        this.placeQueryService = placeQueryService;
        this.reviewQueryService = reviewQueryService;
        this.videoQueryService = videoQueryService;
        this.externalApiExecutor = externalApiExecutor;
    }

    public PlaceResult.Detail getDetailedPlaces(
        Long placeId
    ) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var googlePlaceId = placeQueryService.getGooglePlaceId(placeId);
        if (googlePlaceId.isEmpty()) {
            var placeInfo = placeQueryService.getPlaceInfo(userId, placeId);

            var videoSearchParam = SquareBound.from(
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
            SquareBound.from(placeInfo.longitude().toString(),
                placeInfo.latitude().toString()),
            PageRequest.of(0, 10),
            false
        );
        var videoInfos = videoQueryService.getVideosByPlaceId(placeId);
        var reviewRates = reviewQueryService.getReviewLikeRate(placeId);

        return PlaceResult.Detail.of(placeInfo, googlePlace.join(), videoInfos, reviewRates,
            surroundVideos);
    }

    public CursorResult<Simple> getPlacesInMapRange(
        PlaceParam.Coordinate coordinate,
        PlaceParam.Filter filterParams,
        int size,
        String orderBy,
        Long cursorValue,
        Long cursorId
    ) {
        var userId = AuthorizationUtil.getUserIdOrNull();

        var detailedPlaces = placeQueryService.getPlacesInMapRange(
            userId,
            coordinate,
            filterParams,
            size,
            orderBy,
            cursorValue,
            cursorId
        );

        var placeIds = detailedPlaces.value()
            .stream()
            .map(PlaceQueryResult.DetailedPlace::placeId)
            .toList();
        var placeVideos = videoQueryService.getVideosByPlaceId(placeIds);

        var result = detailedPlaces.value()
            .stream()
            .map(place -> PlaceResult.Simple.of(place, placeVideos.get(place.placeId())))
            .toList();

        return new CursorResult<>(
            result,
            detailedPlaces.hasNext(),
            detailedPlaces.nextCursorValue(),
            detailedPlaces.nextCursorId()
        );
    }

    public Page<PlaceResult.Simple> getPlacesByName(
        String name,
        PlaceParam.Filter filterParams,
        Pageable pageable
    ) {
        var userId = AuthorizationUtil.getUserIdOrNull();

        var placeSimpleInfos = placeQueryService.getPlacesByName(userId, name, filterParams,
            pageable);
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

    public PlaceResult.Category findCategoryById(Long categoryId) {
        return placeQueryService.findCategoryById(categoryId);
    }

    public List<PlaceResult.Category> getCategories() {
        return placeQueryService.getCategories();
    }

    public List<PlaceResult.Category> getSubCategoriesByParentId(Long parentCategoryId) {
        return placeQueryService.getSubCategoriesByParentId(parentCategoryId);
    }

    public List<PlaceResult.Category> findSubCategories() {
        return placeQueryService.getSubCategories();
    }

    public List<PlaceResult.Category> findParentCategories() {
        return placeQueryService.getParentCategories();
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
    public PlaceResult.MarkerDetail getMarkerDetail(
        Long placeId
    ) {
        MarkerDetail markerDetail = placeQueryService.getMarkerInfo(placeId);
        var videos = videoQueryService.getVideosByPlaceId(placeId);
        return PlaceResult.MarkerDetail.of(markerDetail, videos);
    }

    public List<PlaceResult.Marker> getPlaceLocations(
        PlaceParam.Coordinate coordinateParams,
        PlaceParam.Filter filterParams
    ) {
        return placeQueryService.getPlaceLocations(coordinateParams, filterParams)
            .stream()
            .map(PlaceResult.Marker::from)
            .toList();
    }

    public List<PlaceResult.Marker> getPlaceLocationsByName(
        String name,
        PlaceParam.Filter filterParams
    ) {
        return placeQueryService.getPlaceLocationsByName(name, filterParams)
            .stream()
            .map(PlaceResult.Marker::from)
            .toList();
    }

    public List<PlaceResult.Region> getRegions() {
        return placeQueryService.getRegions();
    }
}
