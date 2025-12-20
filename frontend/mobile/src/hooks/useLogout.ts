import * as SecureStore from "expo-secure-store";
import WebView from "react-native-webview";

export const useLogout = (webViewRef: React.RefObject<WebView | null>) => {
  const handleLogout = async () => {
    try {
      // 로컬 토큰 삭제
      await Promise.all([
        SecureStore.deleteItemAsync("accessToken"),
        SecureStore.deleteItemAsync("refreshToken"),
      ]);

      // 웹뷰 초기화
      if (webViewRef.current) {
        const script = `
          window.localStorage.clear();
          window.location.href = '/';
          true;
        `;
        webViewRef.current.injectJavaScript(script);
      }

      console.log("로그아웃 완료");
    } catch (error) {
      console.error("로그아웃 처리 중 오류:", error);
    }
  };

  return { handleLogout };
};
