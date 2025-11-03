import { login } from "@react-native-kakao/user";
import { getAccessToken } from "../api/getAccessToken";
import * as SecureStore from "expo-secure-store";
import WebView from "react-native-webview";

export const useRefreshToken = (
  webViewRef: React.RefObject<WebView | null>
) => {
  const handleRefreshToken = async () => {
    try {
      const refreshToken = await SecureStore.getItemAsync("refreshToken");
      if (!refreshToken) throw new Error("Refresh token not found.");

      // const newTokens = await refreshAuthToken(refreshToken);
      // todo
      const newTokens = {
        accessToken: "",
        refreshToken: "",
      };

      await SecureStore.setItemAsync("accessToken", newTokens.accessToken);
      await SecureStore.setItemAsync("refreshToken", newTokens.refreshToken);

      if (webViewRef.current) {
        const script = `
          window.localStorage.setItem('authToken', '${newTokens.accessToken}');
          window.setAuthToken('${newTokens.accessToken}');
          true;
        `;
        webViewRef.current.injectJavaScript(script);
        console.log("토큰 갱신 성공 및 웹뷰에 주입 완료");
      }
    } catch (error) {
      console.error("토큰 갱신 실패, 로그아웃을 실행합니다:", error);
      try {
        await Promise.all([
          SecureStore.deleteItemAsync("accessToken"),
          SecureStore.deleteItemAsync("refreshToken"),
        ]);
        if (webViewRef.current) {
          const script = `
            window.localStorage.clear();
            window.location.href = '/';
            true;
          `;
          webViewRef.current.injectJavaScript(script);
        }
      } catch (logoutError) {
        console.error("로그아웃 처리 중 추가 오류 발생:", logoutError);
      }
    }
  };
  return { handleRefreshToken };
};
