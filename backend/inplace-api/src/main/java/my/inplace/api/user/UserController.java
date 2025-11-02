package my.inplace.api.user;

import lombok.RequiredArgsConstructor;
import my.inplace.application.user.UserFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import my.inplace.api.user.dto.UserRequest;
import my.inplace.api.user.dto.UserResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
public class UserController implements UserControllerApiSpec {

    public final UserFacade userFacade;

    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateNickname(
        @RequestBody UserRequest.UpdateNickname request
    ) {
        userFacade.updateNickname(request.nickname());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping("/posts/my")
    public ResponseEntity<Page<UserResponse.SimplePost>> getMyPosts(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var posts = userFacade.getMyPosts(pageable);
        
        var response = posts.map(UserResponse.SimplePost::from);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/posts/likes")
    public ResponseEntity<Page<UserResponse.SimplePost>> getMyLikedPosts(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var posts = userFacade.getMyLikedPosts(pageable);
        
        var response = posts.map(UserResponse.SimplePost::from);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/posts/comments")
    public ResponseEntity<Page<UserResponse.SimplePost>> getMyCommentedPosts(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var posts = userFacade.getMyCommentedPosts(pageable);
        
        var response = posts.map(UserResponse.SimplePost::from);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/influencers")
    public ResponseEntity<Page<UserResponse.LikedInfluencer>> getMyFavoriteInfluencers(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var responses = userFacade.getMyFavoriteInfluencers(pageable)
            .map(UserResponse.LikedInfluencer::from);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/places")
    public ResponseEntity<Page<UserResponse.LikedPlace>> getMyFavoritePlaces(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var response = userFacade.getMyFavoritePlaces(pageable)
            .map(UserResponse.LikedPlace::from);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/reviews")
    public ResponseEntity<Page<UserResponse.Review>> getMyReviews(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var responses = userFacade.getMyReviews(pageable)
            .map(UserResponse.Review::from);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<UserResponse.Detail> getUserDetail() {
        var userInfo = userFacade.getUserDetail();

        var response = UserResponse.Detail.from(userInfo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @DeleteMapping()
    public ResponseEntity<Void> deleteUser() {
        userFacade.deleteUser();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/main-badge")
    public ResponseEntity<Void> updateMainBadge(UserRequest.UpdateMainBadge request) {
        userFacade.updateMainBadge(request.id());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/resent/report")
    public ResponseEntity<Void> updateReportPushResent(UserRequest.UpdatePushResent resent) {
        userFacade.updateReportResent(resent.isResented());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PatchMapping("/resent/mention")
    public ResponseEntity<Void> updateMentionPushResent(UserRequest.UpdatePushResent resent) {
        userFacade.updateMentionResent(resent.isResented());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
