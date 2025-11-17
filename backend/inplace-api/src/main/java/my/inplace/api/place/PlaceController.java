package my.inplace.api.place;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.inplace.api.place.dto.PlaceResponse.Regions;
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
import my.inplace.application.place.command.PlaceCommandService;
import my.inplace.application.place.command.dto.PlaceCommand;
import my.inplace.api.place.dto.PlaceRequest;
import my.inplace.api.place.dto.PlaceRequest.Upsert;
import my.inplace.api.place.dto.PlaceResponse;
import my.inplace.api.place.dto.PlaceResponse.Admin;
import my.inplace.api.place.dto.PlaceResponse.AdminCategory;
import my.inplace.api.place.dto.PlaceResponse.Categories;
import my.inplace.api.place.dto.PlaceResponse.Marker;
import my.inplace.api.place.dto.PlaceResponse.MarkerDetail;
import my.inplace.api.place.dto.PlaceResponse.Simple;
import my.inplace.api.place.dto.ReviewResponse;
import my.inplace.application.place.query.PlaceQueryFacade;
import my.inplace.application.place.query.dto.PlaceResult;
import my.inplace.application.review.ReviewService;

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
        var command = request.toCreateCommand();
        placeCommandService.createPlace(command);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<Simple>> getPlaces(
        @ModelAttribute @Validated PlaceRequest.Coordinate coordinateParams,
        @ModelAttribute PlaceRequest.Filter placeFilter,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var placeSimpleInfos = placeQueryFacade.getPlacesInMapRange(
            coordinateParams.toParam(),
            placeFilter.toParam(),
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
        @ModelAttribute PlaceRequest.Filter placeFilter,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var placeSimpleInfos = placeQueryFacade.getPlacesByName(placeName, placeFilter.toParam(),
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
        @ModelAttribute PlaceRequest.Filter placeFilter
    ) {
        List<PlaceResult.Marker> placeMarkerInfos = placeQueryFacade.getPlaceLocations(
            coordinateParams.toParam(),
            placeFilter.toParam()
        );

        return new ResponseEntity<>(
            PlaceResponse.Marker.from(placeMarkerInfos),
            HttpStatus.OK
        );
    }

    @Override
    @GetMapping("/all/search")
    public ResponseEntity<List<Marker>> getPlaceLocationsByName(
        @RequestParam(required = true) String placeName,
        @ModelAttribute PlaceRequest.Filter placeFilter
    ) {
        List<PlaceResult.Marker> placeMarkerInfos = placeQueryFacade.getPlaceLocationsByName(
            placeName,
            placeFilter.toParam()
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
        PlaceResult.MarkerDetail markerDetail = placeQueryFacade.getMarkerDetail(placeId);
        MarkerDetail markerDetailResponse = MarkerDetail.from(markerDetail);
        return new ResponseEntity<>(markerDetailResponse, HttpStatus.OK);
    }

    @Override
    @GetMapping("/categories")
    public ResponseEntity<Categories> getCategories() {
        List<PlaceResult.Category> categories = placeQueryFacade.getCategories();
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
        List<Admin> responses = placeQueryFacade.getAdminPlacesByVideoId(videoId)
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
        Long updatedPlaceId = placeCommandService.updatePlaceInfo(update.toUpdateCommand(placeId));

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

    @Override
    @GetMapping("/regions")
    public ResponseEntity<Regions> getRegions() {
        List<PlaceResult.Region> regions = placeQueryFacade.getRegions();
        var response = Regions.from(regions);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
