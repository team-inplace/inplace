package my.inplace.api.place;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import my.inplace.api.place.dto.PlaceRequest;
import my.inplace.api.place.dto.PlaceRequest.Upsert;
import my.inplace.api.place.dto.PlaceResponse;
import my.inplace.api.place.dto.PlaceResponse.Admin;
import my.inplace.api.place.dto.PlaceResponse.AdminCategory;
import my.inplace.api.place.dto.PlaceResponse.Categories;
import my.inplace.api.place.dto.PlaceResponse.Marker;
import my.inplace.api.place.dto.ReviewResponse;

public interface PlaceControllerApiSpec {

    @Operation(summary = "장소 저장", description = "장소 정보를 저장합니다.")
    ResponseEntity<Void> savePlace(
        @RequestBody Upsert request
    );

    @Operation(summary = "장소 조회", description = "지도 반경 내 혹은 필터링 기준의 장소 페이지네이션 목록을 조회합니다.")
    ResponseEntity<Page<PlaceResponse.Simple>> getPlaces(
        @ModelAttribute @Validated PlaceRequest.Coordinate coordinateParams,
        @ModelAttribute PlaceRequest.Filter filterParams,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    );

    @Operation(summary = "장소 조회(장소 이름으로 검색했을 때)", description = "장소 이름으로 검색한 장소 페이지네이션 목록을 조회합니다.")
    ResponseEntity<Page<PlaceResponse.Simple>> getPlacesByName(
        @RequestParam String placeName,
        @ModelAttribute PlaceRequest.Filter filterParams,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    );

    @Operation(summary = "모든 장소 위치 조회(필터링만)", description = "지도 반경 내 혹은 필터링 기준의 모든 장소 목록을 조회합니다.")
    ResponseEntity<List<Marker>> getPlaceLocations(
        @ModelAttribute @Validated PlaceRequest.Coordinate coordinateParams,
        @ModelAttribute PlaceRequest.Filter filterParams
    );

    @Operation(summary = "모든 장소 위치 조회(장소 이름으로 검색했을 때)", description = "장소 이름으로 검색한 모든 장소 목록을 조회합니다.")
    ResponseEntity<List<Marker>> getPlaceLocationsByName(
        @RequestParam String placeName,
        @ModelAttribute PlaceRequest.Filter filterParams
    );

    @Operation(summary = "카테고리 조회", description = "장소의 카테고리 목록을 조회합니다.")
    ResponseEntity<Categories> getCategories();

    @Operation(summary = "장소 상세 조회", description = "장소 ID를 통해 특정 장소의 상세 정보를 조회합니다.")
    ResponseEntity<PlaceResponse.Detail> getPlaceDetail(
        @PathVariable("id") Long placeId
    );

    @Operation(summary = "장소에 좋아요 누르기", description = "userId와 placeId를 연동하여 장소에 좋아요를 표시합니다.")
    ResponseEntity<Void> likeToPlace(
        @RequestBody PlaceRequest.Like param
    );

    @Operation(summary = "특정 장소 리뷰 조회", description = "페이지네이션이 적용된 특정 장소 리뷰를 조회합니다.")
    ResponseEntity<Page<ReviewResponse.Simple>> getReviews(
        @PathVariable("id") Long placeId,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    );

    @Operation(summary = "마커 상세 정보 조회", description = "장소 ID를 통해 특정 장소의 마커 상세 정보를 조회합니다.")
    ResponseEntity<PlaceResponse.MarkerDetail> getMarkerDetail(
        @PathVariable("id") Long placeId
    );

    @Operation(summary = "비디오에 대한 장소 조회", description = "비디오 ID를 통해 특정 비디의 장소 상세 정보를 조회합니다.")
    ResponseEntity<List<Admin>> getAdminPlacesByVideoId(
        @PathVariable Long videoId
    );

    @Operation(summary = "장소 정보 삭제", description = "장소 ID를 통해 장소 정보를 삭제합니다.")
    ResponseEntity<Void> deletePlaceById(
        @PathVariable Long placeId
    );

    @Operation(summary = "장소 정보 수정", description = "장소 ID를 통해 장소 정보를 수정합니다.")
    ResponseEntity<Long> updatePlaceInfo(
        @PathVariable Long placeId,
        @RequestBody Upsert update
    );

    @Operation(summary = "서브 카테고리 조회", description = "parentId를 통해 subCategory의 전체 정보를 조회합니다.")
    ResponseEntity<List<AdminCategory>> getSubCategoriesByParentId(
        @PathVariable Long parentId
    );

    @Operation(summary = "카테고리 삭제", description = "Id에 해당하는 카테고리를 삭제합니다.")
    ResponseEntity<Void> deleteCategoryById(
        @PathVariable Long categoryId
    );
}
