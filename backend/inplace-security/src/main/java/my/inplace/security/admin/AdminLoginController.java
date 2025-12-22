package my.inplace.security.admin;

import my.inplace.security.application.AdminUserService;
import my.inplace.security.admin.dto.AdminRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminLoginController {
    
    private final AdminUserService adminUserService;
    
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody AdminRequest.Login loginRequest) {
        adminUserService.registerAdminUser(AdminRequest.Login.toRegisterCommand(loginRequest));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
