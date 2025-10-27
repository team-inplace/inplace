package my.inplace.security.user;


import lombok.RequiredArgsConstructor;
import my.inplace.security.user.dto.MobileUserRequest;
import my.inplace.security.user.dto.MobileUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2/code/kakao")
public class MobileUserController {
    
    private final MobileUserFacade mobileUserFacade;
    
    @PostMapping("/mobile")
    public ResponseEntity<MobileUserResponse.TokenResponse> mobileLogin(
        @RequestBody MobileUserRequest mobileUserRequest
    ) {
        MobileUserResponse.TokenResponse response = MobileUserResponse.TokenResponse.from(
            mobileUserFacade.mobileLogin(mobileUserRequest.accessToken())
        );
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
