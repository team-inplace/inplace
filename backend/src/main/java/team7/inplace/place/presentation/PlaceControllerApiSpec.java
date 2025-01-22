package team7.inplace.place.presentation;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import team7.inplace.place.presentation.dto.CategoriesResponse;
import team7.inplace.place.presentation.dto.PlaceLikeRequest;
import team7.inplace.place.presentation.dto.PlacesResponse;
import team7.inplace.place.presentation.dto.PlacesResponse.Location;
import team7.inplace.place.presentation.dto.ReviewResponse;

public interface PlaceControllerApiSpec {

    @Operation(summary = "장소 조회", description = "위치 기반으로 반경 내의 장소 목록을 조회합니다.")
    ResponseEntity<Page<PlacesResponse.Simple>> getPlaces(
        @RequestParam Double longitude,
        @RequestParam Double latitude,
        @RequestParam Double topLeftLongitude,
        @RequestParam Double topLeftLatitude,
        @RequestParam Double bottomRightLongitude,
        @RequestParam Double bottomRightLatitude,
        @RequestParam(required = false) String categories,
        @RequestParam(required = false) String influencers,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    );

    @Operation(summary = "모든 장소 조회", description = "지도 내의 모든 장소 목록을 조회합니다.")
    ResponseEntity<List<Location>> getPlaceLocations(
        @RequestParam Double longitude,
        @RequestParam Double latitude,
        @RequestParam Double topLeftLongitude,
        @RequestParam Double topLeftLatitude,
        @RequestParam Double bottomRightLongitude,
        @RequestParam Double bottomRightLatitude,
        @RequestParam(required = false) String categories,
        @RequestParam(required = false) String influencers
    );

    @Operation(summary = "카테고리 조회", description = "장소의 카테고리 목록을 조회합니다.")
    ResponseEntity<CategoriesResponse> getCategories();

    @Operation(summary = "장소 상세 조회", description = "장소 ID를 통해 특정 장소의 상세 정보를 조회합니다.")
    ResponseEntity<PlacesResponse.Detail> getPlaceDetail(
        @PathVariable("id") Long placeId
    );

    @Operation(summary = "장소에 좋아요 누르기", description = "userId와 placeId를 연동하여 장소에 좋아요를 표시합니다.")
    ResponseEntity<Void> likeToPlace(
        @RequestBody PlaceLikeRequest param
    );

    @Operation(summary = "특정 장소 리뷰 조회", description = "페이지네이션이 적용된 특정 장소 리뷰를 조회합니다.")
    ResponseEntity<Page<ReviewResponse.Simple>> getReviews(
        @PathVariable("id") Long placeId,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    );
}
