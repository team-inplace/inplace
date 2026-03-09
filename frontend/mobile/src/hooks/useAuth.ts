import { login, me } from "@react-native-kakao/user";
import { getAccessToken } from "../api/getAccessToken";
import * as SecureStore from "expo-secure-store";
import WebView from "react-native-webview";
import { useNotification } from "./useNotification";
import { getConfig } from "@inplace-frontend-monorepo/shared/src/api/config";

export const useAuth = (
  webViewRef: React.RefObject<WebView | null>
) => {
  const { getExpoPushToken } = useNotification(webViewRef);

  const sendDeviceToken = async (accessToken: string) => {
    try {
      const expoToken = await getExpoPushToken();
      if (!expoToken) return;

      const config = getConfig();
      await fetch(`${config.baseURL}/alarms`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
        body: JSON.stringify({ fcmToken: null, expoToken }),
      });
    } catch (error) {
      console.error("디바이스 토큰 전송 실패:", error);
    }
  };

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
        const { accessToken, refreshToken, isFirstUser } = tokens;

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

              ${isFirstUser
                ? `window.localStorage.setItem('isFirstUser', 'true');
                   window.location.href = '/choice';`
                : `const redirectPath = window.localStorage.getItem('redirectPath');
                  if (redirectPath) {
                    window.localStorage.removeItem('redirectPath');
                    window.location.href = redirectPath;
                  } else {
                    window.location.reload();
                  }`
              }
            })();
            true;
          `;
          webViewRef.current.injectJavaScript(script);

          console.log("로그인 성공 및 웹뷰에 토큰 주입 완료!");

          await sendDeviceToken(accessToken);
        }
      }
    } catch (error) {
      console.error("카카오 로그인 실패:", error);
      alert(`카카오 로그인에 실패했습니다.`);
    }
  };
  return { handleKakaoLogin };
};
