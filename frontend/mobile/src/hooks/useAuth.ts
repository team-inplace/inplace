import { login, me } from "@react-native-kakao/user";
import { getAccessToken } from "../api/getAccessToken";
import * as SecureStore from "expo-secure-store";
import WebView from "react-native-webview";

export const useAuth = (webViewRef: React.RefObject<WebView | null>) => {
  const handleKakaoLogin = async () => {
    try {
      await login();

      const userProfile = await me();

      const userInfo = {
        nickname: userProfile.nickname ?? "Guest",
        username: userProfile.email ?? "",
        profileImageUrl: userProfile.thumbnailImageUrl ?? "",
      };

      const tokens = await getAccessToken(userInfo);

      if (tokens) {
        const { accessToken, refreshToken } = tokens;

        await Promise.all([
          SecureStore.setItemAsync("accessToken", accessToken),
          SecureStore.setItemAsync("refreshToken", refreshToken),
        ]);

        if (webViewRef.current) {
          const script = `
            (function() {
              window.localStorage.setItem('authToken', '${accessToken}');
              window.localStorage.setItem('isAuthenticated', 'true');
              window.setAuthToken('${accessToken}');

              const redirectPath = window.localStorage.getItem('redirectPath');
              if (redirectPath) {
                window.localStorage.removeItem('redirectPath');
                window.location.href = redirectPath;
              } else {
                window.location.reload();
              }
            })();
            true;
          `;
          webViewRef.current.injectJavaScript(script);

          console.log("로그인 성공 및 웹뷰에 토큰 주입 완료!");
        }
      }
    } catch (error) {
      console.error("카카오 로그인 실패:", error);
    }
  };
  return { handleKakaoLogin };
};
