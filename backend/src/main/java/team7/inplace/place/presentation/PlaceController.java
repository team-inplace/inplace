package team7.inplace.place.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import team7.inplace.place.application.PlaceFacade;
import team7.inplace.place.application.command.PlaceLikeCommand;
import team7.inplace.place.application.dto.PlaceInfo;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.place.presentation.dto.PlaceRequest;
import team7.inplace.place.presentation.dto.PlaceRequest.Create;
import team7.inplace.place.presentation.dto.PlacesResponse;
import team7.inplace.place.presentation.dto.ReviewResponse;
import team7.inplace.review.application.ReviewService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/places")
public class PlaceController implements PlaceControllerApiSpec {

    private final PlaceFacade placeFacade;
    private final ReviewService reviewService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<Void> savePlace(@RequestBody Create request) {
        var command = request.toCommand();
        placeFacade.createPlace(request.videoId(), command);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<PlacesResponse.Simple>> getPlaces(
        @ModelAttribute @Validated PlaceRequest.Coordinate coordinateParams,
        @ModelAttribute PlaceRequest.Filter filterParams,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        log.info("Param : {}", coordinateParams);
        log.info("Param : {}", filterParams);
        var placeSimpleInfos = placeFacade.getPlacesInMapRange(
            coordinateParams.toCommand(),
            filterParams.toCommand(),
            pageable
        );

        var responses = PlacesResponse.Simple.from(placeSimpleInfos.getContent());
        return new ResponseEntity<>(
            new PageImpl<>(responses, pageable, placeSimpleInfos.getTotalElements()),
            HttpStatus.OK
        );
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<PlacesResponse.Location>> getPlaceLocations(
        @ModelAttribute @Validated PlaceRequest.Coordinate coordinateParams,
        @ModelAttribute PlaceRequest.Filter filterParams
    ) {
        List<PlaceQueryResult.Location> placeLocationInfos = placeFacade.getPlaceLocations(
            coordinateParams.toCommand(),
            filterParams.toCommand()
        );

        return new ResponseEntity<>(
            PlacesResponse.Location.from(placeLocationInfos),
            HttpStatus.OK
        );
    }

    @Override
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PlacesResponse.Detail>> getPlaceDetail(
        @PathVariable("id") Long placeId
    ) {
        return placeFacade.getDetailedPlaces(placeId)
            .map(placeInfo -> {
                var response = PlacesResponse.Detail.from(placeInfo);
                return ResponseEntity.ok(response);
            })
            .doOnError(e -> log.error("오류 발생: ", e));
    }

    @GetMapping("/{id}/marker")
    public ResponseEntity<PlacesResponse.Marker> getPlaceForMarker(
        @PathVariable("id") Long placeId
    ) {
        PlaceInfo.Marker marker = placeFacade.getMarkerInfo(placeId);
        PlacesResponse.Marker markerResponse = PlacesResponse.Marker.from(marker);
        return new ResponseEntity<>(markerResponse, HttpStatus.OK);
    }

    @Override
    @GetMapping("/categories")
    public ResponseEntity<PlacesResponse.Category> getCategories() {
        List<PlaceInfo.Category> categories = placeFacade.getCategories();
        var response = PlacesResponse.Category.from(categories);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @PostMapping("/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> likeToPlace(@RequestBody PlaceRequest.Like param) {
        placeFacade.updateLikedPlace(new PlaceLikeCommand(param.placeId(), param.likes()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}/reviews")
    public ResponseEntity<Page<ReviewResponse.Simple>> getReviews(
        @PathVariable("id") Long placeId,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var responses = reviewService.getPlaceReviews(placeId, pageable)
            .map(ReviewResponse.Simple::from);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
