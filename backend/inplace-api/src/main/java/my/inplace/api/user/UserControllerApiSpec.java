package my.inplace.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import my.inplace.api.user.dto.UserRequest;
import my.inplace.api.user.dto.UserResponse;

public interface UserControllerApiSpec {

    @Operation(
        summary = "유저 닉네임 변경",
        description = "RequestParameter로 받은 유저 닉네임으로 닉네임 변경"
    )
    ResponseEntity<Void> updateNickname(
        @RequestBody UserRequest.UpdateNickname request
    );

    @Operation(summary = "좋아요한 인플루언서 반환", description = "좋아요한 인플루언서를 반환합니다.")
    ResponseEntity<Page<UserResponse.LikedInfluencer>> getMyFavoriteInfluencers(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    );

    @Operation(summary = "좋아요한 장소 반환", description = "좋아요한 장소를 반환합니다.")
    ResponseEntity<Page<UserResponse.LikedPlace>> getMyFavoritePlaces(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    );

    @Operation(summary = "작성한 리뷰 반환", description = "작성한 리뷰를 반환합니다.")
    ResponseEntity<Page<UserResponse.Review>> getMyReviews(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    );

    @Operation(summary = "유저 정보 반환", description = "유저 정보를 반환합니다.")
    ResponseEntity<UserResponse.Detail> getUserDetail();

    @Operation(summary = "회원탈퇴", description = "회원탈퇴를 진행합니다.")
    ResponseEntity<Void> deleteUser();

    @Operation(summary = "메인 뱃지 변경", description = "메인 뱃지를 변경합니다.")
    ResponseEntity<Void> updateMainBadge(UserRequest.UpdateMainBadge request);
}
