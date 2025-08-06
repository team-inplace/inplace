package application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import team7.inplace.admin.user.application.AdminUserService;
import team7.inplace.admin.user.application.dto.AdminUserInfo;
import application.dto.CustomUserDetails;
import team7.inplace.user.application.UserService;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminUserService adminUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUserInfo adminUserInfo = adminUserService.findAdminUserByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User is not found"));

        return CustomUserDetails.makeUser(adminUserInfo);
    }
}
