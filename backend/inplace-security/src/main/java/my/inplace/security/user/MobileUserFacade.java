package my.inplace.security.user;

import lombok.RequiredArgsConstructor;
import my.inplace.security.user.dto.TokenResult;
import my.inplace.security.user.dto.UserSecurityCommand;
import my.inplace.security.user.dto.UserSecurityResult;
import my.inplace.security.util.JwtUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MobileUserFacade {
    
    private final UserSecurityService userSecurityService;
    private final JwtUtil jwtUtil;
    
    public TokenResult mobileLogin(String username, String nickname, String profileImageUrl) {
        Optional<UserSecurityResult.Info> userByUsername = userSecurityService.findUserByUsername(username);
        
        if(userByUsername.isEmpty()) {
            UserSecurityResult.Info info = userSecurityService.registerUser(
                UserSecurityCommand.Create.from(username, nickname, profileImageUrl));

            return makeToken(info.username(), info.id(), info.role().getRoles());
        }
        
        userSecurityService.updateProfileImageUrl(userByUsername.get().id(), profileImageUrl);
        return makeToken(
            userByUsername.get().username(),
            userByUsername.get().id(),
            userByUsername.get().role().getRoles()
        );
    }
    
    private TokenResult makeToken(String username, Long id, String role) {
        String accessToken = jwtUtil.createAccessToken(username, id, role);
        String refreshToken = jwtUtil.createRefreshToken(username, id, role);
        
        return new TokenResult(accessToken, refreshToken);
    }
}
