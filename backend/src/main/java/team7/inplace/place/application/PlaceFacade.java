package team7.inplace.place.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import team7.inplace.global.annotation.Facade;
import team7.inplace.place.application.command.PlaceLikeCommand;
import team7.inplace.place.application.command.PlacesCommand.Coordinate;
import team7.inplace.place.application.command.PlacesCommand.FilterParams;
import team7.inplace.place.application.command.PlacesCommand.Upsert;
import team7.inplace.place.application.dto.PlaceInfo;
import team7.inplace.place.application.dto.PlaceInfo.Simple;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.place.persistence.dto.PlaceQueryResult.Marker;
import team7.inplace.place.persistence.dto.PlaceQueryResult.MarkerDetail;
import team7.inplace.review.application.ReviewService;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.video.application.VideoService;
import team7.inplace.video.presentation.dto.VideoSearchParams;

@Slf4j
@Facade
@RequiredArgsConstructor
public class PlaceFacade {

    private final PlaceService placeService;
    private final ReviewService reviewService;
    private final VideoService videoService;

    private final Executor externalApiExecutor;

    public void createPlace(Upsert command) {
        placeService.createPlace(command);
    }

    public PlaceInfo.Marker getMarkerInfo(Long placeId) {
        MarkerDetail markerDetail = placeService.getMarkerInfo(placeId);
        var videos = videoService.getVideosByPlaceId(placeId);
        return PlaceInfo.Marker.of(markerDetail, videos);
    }

    public PlaceInfo.Detail getDetailedPlaces(Long placeId) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var googlePlaceId = placeService.getGooglePlaceId(placeId);
        if (googlePlaceId.isEmpty()) {
            var placeInfo = placeService.getPlaceInfo(userId, placeId);
            var surroundVideos = videoService.getVideosBySurround(
                VideoSearchParams.from(placeInfo.longitude().toString(),
                    placeInfo.latitude().toString()),
                PageRequest.of(0, 10),
                false
            );
            var videoInfos = videoService.getVideosByPlaceId(placeId);
            var reviewRates = reviewService.getReviewLikeRate(placeId);
            return PlaceInfo.Detail.of(placeInfo, null, videoInfos, reviewRates, surroundVideos);
        }

        var googlePlace = CompletableFuture.supplyAsync(
            () -> placeService.getGooglePlaceInfo(googlePlaceId.get()), externalApiExecutor
        ).exceptionally(e -> null);

        var placeInfo = placeService.getPlaceInfo(userId, placeId);
        var surroundVideos = videoService.getVideosBySurround(
            VideoSearchParams.from(placeInfo.longitude().toString(),
                placeInfo.latitude().toString()),
            PageRequest.of(0, 10),
            false
        );
        var videoInfos = videoService.getVideosByPlaceId(placeId);
        var reviewRates = reviewService.getReviewLikeRate(placeId);

        return PlaceInfo.Detail.of(placeInfo, googlePlace.join(), videoInfos, reviewRates,
            surroundVideos);
    }

    public List<Marker> getPlaceLocations(
        Coordinate coordinateCommand,
        FilterParams filterParamsCommand
    ) {
        return placeService.getPlaceLocations(coordinateCommand, filterParamsCommand);
    }

    public Page<PlaceInfo.Simple> getPlacesInMapRange(
        Coordinate coordinateCommand,
        FilterParams filterParamsCommand,
        Pageable pageable
    ) {
        var userId = AuthorizationUtil.getUserIdOrNull();

        var placeSimpleInfos = placeService.getPlacesInMapRange(
            userId,
            coordinateCommand,
            filterParamsCommand, pageable
        );
        var placeIds = placeSimpleInfos.getContent()
            .stream()
            .map(PlaceQueryResult.DetailedPlace::placeId)
            .toList();
        var placeVideos = videoService.getVideosByPlaceId(placeIds);

        return placeSimpleInfos
            .map(place -> PlaceInfo.Simple.of(place, placeVideos.get(place.placeId())));
    }

    public void updateLikedPlace(PlaceLikeCommand placeLikeCommand) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        placeService.updateLikedPlace(userId, placeLikeCommand);
    }

    public List<PlaceInfo.Category> getCategories() {
        return placeService.getCategories();
    }

    public List<Marker> getPlaceLocationsByName(String name, FilterParams command) {
        return placeService.getPlaceLocationsByName(name, command);
    }

    public Page<Simple> getPlacesByName(String name, FilterParams command, Pageable pageable) {
        var userId = AuthorizationUtil.getUserIdOrNull();

        var placeSimpleInfos = placeService.getPlacesByName(userId, name, command, pageable);
        var placeIds = placeSimpleInfos.getContent()
            .stream()
            .map(PlaceQueryResult.DetailedPlace::placeId)
            .toList();
        var placeVideos = videoService.getVideosByPlaceId(placeIds);

        return placeSimpleInfos
            .map(place -> PlaceInfo.Simple.of(place, placeVideos.get(place.placeId())));
    }

    public List<PlaceInfo.Simple> getAdminPlacesByVideoId(Long videoId) {
        return placeService.getSimplePlacesByVideoId(videoId)
            .stream()
            .map((place) -> Simple.of(place, null))
            .toList();
    }

    public void deletePlaceById(Long placeId) {
        placeService.deletePlaceById(placeId);
    }

    public Long updatePlaceInfo(Long placeId, Upsert command) {
        return placeService.updatePlaceInfo(placeId, command);
    }

    public List<PlaceInfo.Category> getSubCategoriesByParentId(Long parentCategoryId) {
        return placeService.getSubCategoriesByParentId(parentCategoryId);
    }

    public void deleteCategoryById(Long categoryId) {
        placeService.deleteCategoryById(categoryId);
    }
}
