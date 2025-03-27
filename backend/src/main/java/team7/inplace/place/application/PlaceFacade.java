package team7.inplace.place.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import team7.inplace.global.annotation.Facade;
import team7.inplace.influencer.application.InfluencerService;
import team7.inplace.place.application.command.PlaceLikeCommand;
import team7.inplace.place.application.command.PlacesCommand;
import team7.inplace.place.application.command.PlacesCommand.Coordinate;
import team7.inplace.place.application.command.PlacesCommand.FilterParams;
import team7.inplace.place.application.dto.PlaceInfo;
import team7.inplace.place.application.dto.PlaceInfo.Marker;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.place.persistence.dto.PlaceQueryResult.Location;
import team7.inplace.review.application.ReviewService;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.video.application.VideoService;

@Slf4j
@Facade
@RequiredArgsConstructor
public class PlaceFacade {

    private final PlaceService placeService;
    private final InfluencerService influencerService;
    private final ReviewService reviewService;
    private final VideoService videoService;

    public void createPlace(Long videoId, PlacesCommand.Create command) {
        var placeId = placeService.createPlace(command);

        videoService.addPlaceInfo(videoId, placeId);
    }

    public Marker getMarkerInfo(Long placeId) {
        PlaceQueryResult.Marker marker = placeService.getMarkerInfo(placeId);
        var videos = videoService.getVideosByPlaceId(placeId);
        return PlaceInfo.Marker.of(marker, videos);
    }

    public Mono<PlaceInfo.Detail> getDetailedPlaces(Long placeId) {
        var userId = AuthorizationUtil.getUserId();

        var placeInfoMono = Mono.fromCallable(() -> placeService.getPlaceInfo(placeId, userId));

        return placeInfoMono.flatMap(placeInfo -> {
            var videoInfosMono = Mono.fromCallable(
                () -> videoService.getVideosByPlaceId(placeInfo.placeId()));
            var reviewRatesMono = Mono.fromCallable(
                () -> reviewService.getReviewLikeRate(placeInfo.placeId()));

            if (placeInfo.haveNoGooglePlaceId()) {
                return Mono.zip(videoInfosMono, reviewRatesMono)
                    .map(tuple -> PlaceInfo.Detail.of(placeInfo, null, tuple.getT1(),
                        tuple.getT2()));
            }

            var googlePlaceMono = placeService.getGooglePlaceInfo(placeInfo.googlePlaceId());

            return Mono.zip(googlePlaceMono, videoInfosMono, reviewRatesMono)
                .map(tuple -> PlaceInfo.Detail.of(placeInfo, tuple.getT1(), tuple.getT2(),
                    tuple.getT3()));
        });
    }

    public List<PlaceQueryResult.Location> getPlaceLocations(
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
        var userId = AuthorizationUtil.getUserId();

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
        var userId = AuthorizationUtil.getUserId();
        placeService.updateLikedPlace(userId, placeLikeCommand);
    }

    public List<PlaceInfo.Category> getCategories() {
        return placeService.getCategories();
    }

    public List<Location> getPlaceLocationsByName(String name, FilterParams command) {
        return placeService.getPlaceLocationsByName(name, command);
    }
}
