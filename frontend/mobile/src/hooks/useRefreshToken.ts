import * as SecureStore from "expo-secure-store";
import WebView from "react-native-webview";
import { getRefreshAuthToken } from "../api/getRefreshAuthToken";
import { useLogout } from "./useLogout";

export const useRefreshToken = (
  webViewRef: React.RefObject<WebView | null>
) => {
  const { handleLogout } = useLogout(webViewRef);
  const handleRefreshToken = async () => {
    try {
      const refreshToken = await SecureStore.getItemAsync("refreshToken");
      if (!refreshToken) {
        console.error("Refresh token not found. 로그아웃 처리");
        await handleLogout();
        return;
      }

      const newTokens = await getRefreshAuthToken(refreshToken);
      if (!newTokens) {
        console.error("토큰 갱신 실패, 로그아웃 처리");
        await handleLogout();
        return;
      }
      await Promise.all([
        SecureStore.setItemAsync("accessToken", newTokens.accessToken),
        SecureStore.setItemAsync("refreshToken", newTokens.refreshToken),
      ]);

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
      await handleLogout();
    }
  };

  return { handleRefreshToken };
};
