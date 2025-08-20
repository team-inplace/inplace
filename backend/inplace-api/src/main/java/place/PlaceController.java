package place;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import place.command.PlaceCommandService;
import place.command.dto.PlaceCommand;
import place.dto.PlaceRequest;
import place.dto.PlaceRequest.Upsert;
import place.dto.PlaceResponse;
import place.dto.PlaceResponse.Admin;
import place.dto.PlaceResponse.AdminCategory;
import place.dto.PlaceResponse.Categories;
import place.dto.PlaceResponse.Marker;
import place.dto.PlaceResponse.MarkerDetail;
import place.dto.PlaceResponse.Simple;
import place.dto.ReviewResponse;
import place.query.PlaceQueryFacade;
import review.ReviewService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/places")
public class PlaceController implements PlaceControllerApiSpec {

    private final PlaceQueryFacade placeQueryFacade;
    private final PlaceCommandService placeCommandService;
    private final ReviewService reviewService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<Void> savePlace(@RequestBody Upsert request) {
        var command = request.toCommand();
        placeCommandService.createPlace(command);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<PlaceResponse.Simple>> getPlaces(
        @ModelAttribute @Validated PlaceRequest.Coordinate coordinateParams,
        @ModelAttribute PlaceRequest.Filter filterParams,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var placeSimpleInfos = placeQueryFacade.getPlacesInMapRange(
            coordinateParams.toCommand(),
            filterParams.toCommand(),
            pageable
        );

        var responses = PlaceResponse.Simple.from(placeSimpleInfos.getContent());
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
        var placeSimpleInfos = placeQueryFacade.getPlacesByName(placeName, filterParams.toCommand(),
            pageable);
        var responses = PlaceResponse.Simple.from(placeSimpleInfos.getContent());
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
        List<PlaceQueryResult.Marker> placeMarkerInfos = placeQueryFacade.getPlaceLocations(
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
        List<PlaceQueryResult.Marker> placeMarkerInfos = placeQueryFacade.getPlaceLocationsByName(
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
    public ResponseEntity<PlaceResponse.Detail> getPlaceDetail(
        @PathVariable("id") Long placeId
    ) {
        var placeDetail = placeQueryFacade.getDetailedPlaces(placeId);

        var response = PlaceResponse.Detail.from(placeDetail);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}/marker")
    public ResponseEntity<MarkerDetail> getMarkerDetail(
        @PathVariable("id") Long placeId
    ) {
        PlaceInfo.Marker marker = placeQueryFacade.getMarkerInfo(placeId);
        MarkerDetail markerDetailResponse = MarkerDetail.from(marker);
        return new ResponseEntity<>(markerDetailResponse, HttpStatus.OK);
    }

    @Override
    @GetMapping("/categories")
    public ResponseEntity<Categories> getCategories() {
        List<PlaceInfo.Category> categories = placeQueryFacade.getCategories();
        var response = Categories.from(categories);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @PostMapping("/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> likeToPlace(@RequestBody PlaceRequest.Like param) {
        placeCommandService.updateLikedPlace(new PlaceCommand.Like(param.placeId(), param.likes()));
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

    @Override
    @GetMapping("/videos/{videoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Admin>> getAdminPlacesByVideoId(
        @PathVariable Long videoId
    ) {
        List<Admin> responses = placeFacade.getAdminPlacesByVideoId(videoId)
            .stream()
            .map(Admin::of)
            .toList();

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{placeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePlaceById(
        @PathVariable Long placeId
    ) {
        placeCommandService.deletePlaceById(placeId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PutMapping("/{placeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> updatePlaceInfo(
        @PathVariable Long placeId,
        @RequestBody Upsert update
    ) {
        Long updatedPlaceId = placeCommandService.updatePlaceInfo(placeId, update.toCommand());

        return new ResponseEntity<>(updatedPlaceId, HttpStatus.OK);
    }

    @Override
    @GetMapping("/categories/parent/{parentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminCategory>> getSubCategoriesByParentId(
        @PathVariable Long parentId
    ) {
        List<AdminCategory> subCategories = placeQueryFacade.getSubCategoriesByParentId(parentId)
            .stream()
            .map(AdminCategory::of)
            .toList();

        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategoryById(
        @PathVariable Long categoryId
    ) {
        placeCommandService.deleteCategoryById(categoryId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
