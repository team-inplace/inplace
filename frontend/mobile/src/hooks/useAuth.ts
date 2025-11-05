import { login } from "@react-native-kakao/user";
import { getAccessToken } from "../api/getAccessToken";
import * as SecureStore from "expo-secure-store";
import WebView from "react-native-webview";

export const useAuth = (webViewRef: React.RefObject<WebView | null>) => {
  const handleKakaoLogin = async () => {
    try {
      const kakaoToken = await login();

      const tokens = await getAccessToken(kakaoToken.accessToken);

      if (tokens) {
        const { accessToken, refreshToken } = tokens;

        await Promise.all([
          SecureStore.setItemAsync("accessToken", accessToken),
          SecureStore.setItemAsync("refreshToken", refreshToken),
        ]);

        if (webViewRef.current) {
          const script = `
          window.localStorage.setItem('authToken', '${accessToken}');
          window.setAuthToken('${accessToken}');
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
