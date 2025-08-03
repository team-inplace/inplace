package place;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import place.dto.PlaceRequest;
import place.dto.PlaceRequest.Upsert;
import place.dto.PlacesResponse;
import place.dto.PlacesResponse.Admin;
import place.dto.PlacesResponse.AdminCategory;
import place.dto.PlacesResponse.Categories;
import place.dto.PlacesResponse.Marker;
import place.dto.PlacesResponse.MarkerDetail;
import place.dto.PlacesResponse.Simple;
import place.dto.ReviewResponse;
import place.query.PlaceQueryResult;
import team7.inplace.place.application.PlaceFacade;
import team7.inplace.place.application.command.PlaceLikeCommand;
import team7.inplace.place.application.dto.PlaceInfo;
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
    public ResponseEntity<Void> savePlace(@RequestBody Upsert request) {
        var command = request.toCommand();
        placeFacade.createPlace(command);

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
    public ResponseEntity<Categories> getCategories() {
        List<PlaceInfo.Category> categories = placeFacade.getCategories();
        var response = Categories.from(categories);

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
        placeFacade.deletePlaceById(placeId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PutMapping("/{placeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> updatePlaceInfo(
        @PathVariable Long placeId,
        @RequestBody Upsert update
    ) {
        Long updatedPlaceId = placeFacade.updatePlaceInfo(placeId, update.toCommand());

        return new ResponseEntity<>(updatedPlaceId, HttpStatus.OK);
    }

    @Override
    @GetMapping("/categories/parent/{parentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminCategory>> getSubCategoriesByParentId(
        @PathVariable Long parentId
    ) {
        List<AdminCategory> subCategories = placeFacade.getSubCategoriesByParentId(parentId)
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
        placeFacade.deleteCategoryById(categoryId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
