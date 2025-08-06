package application;

import admin.AdminUserResult;
import admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import application.dto.CustomUserDetails;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminUserService adminUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUserResult adminUserresult = adminUserService.findAdminUserByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User is not found"));

        return CustomUserDetails.makeUser(adminUserresult);
    }
}
