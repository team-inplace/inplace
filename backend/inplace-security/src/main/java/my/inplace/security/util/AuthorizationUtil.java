package my.inplace.security.util;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.AuthorizationErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import my.inplace.security.application.dto.CustomOAuth2User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizationUtil {

    public static Long getUserIdOrNull() {
        if (isNotLoginUser()) {
            return null;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        return customOAuth2User.id();
    }

    public static Long getUserIdOrThrow() {
        checkLoginUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        return customOAuth2User.id();
    }

    public static boolean isNotLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication.getPrincipal() instanceof CustomOAuth2User);
    }

    public static void checkLoginUser() {
        if (isNotLoginUser()) {
            throw InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY);
        }
    }
}
