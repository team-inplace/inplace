package my.inplace.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.util.List;
import my.inplace.api.user.dto.UserResponse.BadgeWithOwnerShip;
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
    
    @Operation(summary = "내가 작성한 글 반환", description = "내가 작성한 글들을 반환합니다.")
    ResponseEntity<Page<UserResponse.SimplePost>> getMyPosts(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    );
    
    @Operation(summary = "내가 좋아요 한 글 반환", description = "내가 좋아요 한 글들을 반환합니다.")
    ResponseEntity<Page<UserResponse.SimplePost>> getMyLikedPosts(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    );
    
    @Operation(summary = "내가 댓글 단 글 반환", description = "내가 댓글을 단 글들을 반환합니다.")
    public ResponseEntity<Page<UserResponse.SimplePost>> getMyCommentedPosts(
        @PageableDefault(page = 0, size = 10) Pageable pageable
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
    ResponseEntity<UserResponse.Info> getUserDetail();

    @Operation(summary = "모든 칭호 반환", description = "모든 칭호를 user 소유 여부, user 대표 칭호 여부와 함께 반환합니다.")
    ResponseEntity<List<BadgeWithOwnerShip>> getAllBadgesWithOwnerShip();

    @Operation(summary = "회원탈퇴", description = "회원탈퇴를 진행합니다.")
    ResponseEntity<Void> deleteUser();

    @Operation(summary = "메인 뱃지 변경", description = "메인 뱃지를 변경합니다.")
    ResponseEntity<Void> updateMainBadge(UserRequest.UpdateMainBadge request);
    
    @Operation(summary = "신고에 대한 푸시 알림 허용/거부", description = "신고에 대한 푸시 알림을 허용하거나 거부합니다.")
    ResponseEntity<Void> updateReportPushResent(UserRequest.UpdatePushResent resent);
    
    @Operation(summary = "언급에 대한 푸시 알림 허용/거부", description = "언급에 대한 푸시 알림을 허용하거나 거부합니다.")
    ResponseEntity<Void> updateMentionPushResent(UserRequest.UpdatePushResent resent);
}
