package my.inplace.security.application;

import lombok.RequiredArgsConstructor;


import my.inplace.security.application.dto.AdminResult;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import my.inplace.security.application.dto.CustomUserDetails;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminUserService adminUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminResult.AuthInfo adminAuthInfo = adminUserService.findAdminUserByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User is not found"));

        return CustomUserDetails.makeUser(adminAuthInfo);
    }
}
