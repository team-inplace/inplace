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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.place.application.PlaceFacade;
import team7.inplace.place.application.command.PlaceLikeCommand;
import team7.inplace.place.application.dto.PlaceInfo;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.place.presentation.dto.PlaceRequest;
import team7.inplace.place.presentation.dto.PlaceRequest.Create;
import team7.inplace.place.presentation.dto.PlacesResponse;
import team7.inplace.place.presentation.dto.PlacesResponse.Marker;
import team7.inplace.place.presentation.dto.PlacesResponse.MarkerDetail;
import team7.inplace.place.presentation.dto.PlacesResponse.Simple;
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
    @GetMapping("/search")
    public ResponseEntity<Page<Simple>> getPlacesByName(
        @RequestParam String placeName,
        @ModelAttribute PlaceRequest.Filter filterParams,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var placeSimpleInfos = placeFacade.getPlacesByName(placeName, filterParams.toCommand(),
            pageable);
        var responses = PlacesResponse.Simple.from(placeSimpleInfos.getContent());
        return new ResponseEntity<>(
            new PageImpl<>(responses, pageable, placeSimpleInfos.getTotalElements()),
            HttpStatus.OK
        );
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<Marker>> getPlaceLocations(
        @ModelAttribute @Validated PlaceRequest.Coordinate coordinateParams,
        @ModelAttribute PlaceRequest.Filter filterParams
    ) {
        List<PlaceQueryResult.Marker> placeMarkerInfos = placeFacade.getPlaceLocations(
            coordinateParams.toCommand(),
            filterParams.toCommand()
        );

        return new ResponseEntity<>(
            Marker.from(placeMarkerInfos),
            HttpStatus.OK
        );
    }

    @Override
    @GetMapping("/all/search")
    public ResponseEntity<List<Marker>> getPlaceLocationsByName(
        @RequestParam(required = true) String placeName,
        @ModelAttribute PlaceRequest.Filter filterParams
    ) {
        List<PlaceQueryResult.Marker> placeMarkerInfos = placeFacade.getPlaceLocationsByName(
            placeName,
            filterParams.toCommand()
        );

        return new ResponseEntity<>(
            Marker.from(placeMarkerInfos),
            HttpStatus.OK
        );
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PlacesResponse.Detail> getPlaceDetail(
        @PathVariable("id") Long placeId
    ) {
        var placeDetail = placeFacade.getDetailedPlaces(placeId);

        var response = PlacesResponse.Detail.from(placeDetail);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}/marker")
    public ResponseEntity<MarkerDetail> getMarkerDetail(
        @PathVariable("id") Long placeId
    ) {
        PlaceInfo.Marker marker = placeFacade.getMarkerInfo(placeId);
        MarkerDetail markerDetailResponse = MarkerDetail.from(marker);
        return new ResponseEntity<>(markerDetailResponse, HttpStatus.OK);
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
