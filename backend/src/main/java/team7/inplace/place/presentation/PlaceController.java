package team7.inplace.place.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.kakao.application.PlaceMessageFacade;
import team7.inplace.place.application.CategoryService;
import team7.inplace.place.application.PlaceService;
import team7.inplace.place.application.command.PlaceLikeCommand;
import team7.inplace.place.application.command.PlacesCommand;
import team7.inplace.place.application.dto.CategoryInfo;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.place.presentation.dto.CategoriesResponse;
import team7.inplace.place.presentation.dto.PlaceLikeRequest;
import team7.inplace.place.presentation.dto.PlacesResponse;
import team7.inplace.place.presentation.dto.ReviewResponse;
import team7.inplace.review.application.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/places")
public class PlaceController implements PlaceControllerApiSpec {

    private final PlaceMessageFacade placeMessageFacade;
    private final PlaceService placeService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<Page<PlacesResponse.Simple>> getPlaces(
        @RequestParam Double longitude,
        @RequestParam Double latitude,
        @RequestParam Double topLeftLongitude,
        @RequestParam Double topLeftLatitude,
        @RequestParam Double bottomRightLongitude,
        @RequestParam Double bottomRightLatitude,
        @RequestParam(required = false) String categories,
        @RequestParam(required = false) String influencers,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var placeSimpleInfos = placeService.getPlacesInMapRange(
            new PlacesCommand.Coordinate(
                topLeftLongitude, topLeftLatitude,
                bottomRightLongitude, bottomRightLatitude,
                longitude, latitude
            ),
            new PlacesCommand.FilterParams(categories, influencers),
            pageable
        );

        var responses = PlacesResponse.Simple.from(placeSimpleInfos.getContent());
        return new ResponseEntity<>(
            new PageImpl<>(responses, pageable, placeSimpleInfos.getTotalElements()),
            HttpStatus.OK
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<PlacesResponse.Location>> getPlaceLocations(
        @RequestParam Double longitude,
        @RequestParam Double latitude,
        @RequestParam Double topLeftLongitude,
        @RequestParam Double topLeftLatitude,
        @RequestParam Double bottomRightLongitude,
        @RequestParam Double bottomRightLatitude,
        @RequestParam(required = false, defaultValue = "") String categories,
        @RequestParam(required = false, defaultValue = "") String influencers
    ) {
        List<PlaceQueryResult.Location> placeLocationInfos = placeService.getPlaceLocations(
            new PlacesCommand.Coordinate(
                topLeftLongitude, topLeftLatitude,
                bottomRightLongitude, bottomRightLatitude,
                longitude, latitude
            ),
            new PlacesCommand.FilterParams(categories, influencers)
        );

        return new ResponseEntity<>(
            PlacesResponse.Location.from(placeLocationInfos),
            HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlacesResponse.Detail> getPlaceDetail(
        @PathVariable("id") Long placeId
    ) {
        var placeDetail = placeService.getPlaceDetailInfo(placeId);

        var response = PlacesResponse.Detail.from(placeDetail);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<CategoriesResponse> getCategories() {
        List<CategoryInfo> categories = categoryService.getCategories();
        CategoriesResponse response = new CategoriesResponse(categories);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> likeToPlace(@RequestBody PlaceLikeRequest param) {
        placeService.updateLikedPlace(new PlaceLikeCommand(param.placeId(), param.likes()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

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
