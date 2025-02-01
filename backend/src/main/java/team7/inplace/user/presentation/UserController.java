package team7.inplace.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.user.application.UserFacade;
import team7.inplace.user.application.UserService;
import team7.inplace.user.presentation.dto.UserResponse;

//TODO: RequestMapping적용 -> /users
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserController implements UserControllerApiSepc {

    public final UserService userService;
    public final UserFacade userFacade;

    @PatchMapping("/user/nickname")
    public ResponseEntity<Void> updateNickname(
        @PathVariable("nickname") String nickname
    ) {
        userService.updateNickname(nickname);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users/influencers")
    public ResponseEntity<Page<UserResponse.LikedInfluencer>> getMyFavoriteInfluencers(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var responses = userFacade.getMyFavoriteInfluencers(pageable)
            .map(UserResponse.LikedInfluencer::from);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/users/places")
    public ResponseEntity<Page<UserResponse.LikedPlace>> getMyFavoritePlaces(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var response = userFacade.getMyFavoritePlaces(pageable)
            .map(UserResponse.LikedPlace::from);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/users/reviews")
    public ResponseEntity<Page<UserResponse.Review>> getMyReviews(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        var responses = userFacade.getMyReviews(pageable)
            .map(UserResponse.Review::from);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/users/info")
    public ResponseEntity<UserResponse.Info> getUserInfo() {
        var userInfo = userService.getUserInfo();

        var response = UserResponse.Info.from(userInfo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
