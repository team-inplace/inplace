package my.inplace.api.security;

import my.inplace.application.security.admin.AdminUserService;
import my.inplace.api.security.dto.AdminRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminLoginController {

    private final AuthenticationManager authenticationManager;
    private final AdminUserService adminUserService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody AdminRequest.Login loginRequest) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(
            loginRequest.username(),
            loginRequest.password());
        authenticationManager.authenticate(authenticationRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody AdminRequest.Login loginRequest) {
        adminUserService.registerAdminUser(AdminRequest.Login.toRegisterCommand(loginRequest));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
