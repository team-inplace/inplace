import { Platform } from "react-native";
import WebView from "react-native-webview";
import * as Notifications from "expo-notifications";

export const useNotification = (webViewRef: React.RefObject<WebView | null>) => {
  const handleNotificationPermission = async (): Promise<void> => {
    try {
      alert("[DEBUG] 1. 알림 권한 요청 시작");

      const { status: existingStatus } =
        await Notifications.getPermissionsAsync();
      alert(`[DEBUG] 2. 현재 권한 상태: ${existingStatus}`);
      const finalStatus = existingStatus !== "granted" // 권한이 없으면 요청(없거나 거부된 상태)
        ? (await Notifications.requestPermissionsAsync()).status
        : existingStatus;

      alert(`[DEBUG] 3. 최종 권한 상태: ${finalStatus}`);
      if (finalStatus !== "granted") {
        console.log("알림 권한이 거부되었습니다");
        alert("[ERROR] 알림 권한이 거부되었습니다");
        return;
      }
      // 안드로이드에서 채널 설정
      if (Platform.OS === "android") {
        await Notifications.setNotificationChannelAsync("default", {
          name: "default",
          importance: Notifications.AndroidImportance.MAX,
          vibrationPattern: [0, 250, 250, 250],
          lightColor: "#FF231F7C",
        });
      }

      alert("[DEBUG] 4. 권한 승인됨, Expo Token 요청 시작");

      // 권한이 승인된 상태에서만 토큰 요청
      await getExpoPushToken();
    } catch (error) {
      console.error("알림 권한 요청 실패:", error);
      alert(`[ERROR] 알림 권한 요청 실패: ${error}`);
    }
  };

  const getExpoPushToken = async () => {
    try {
      alert("[DEBUG] 5. getExpoPushTokenAsync 호출 시작");

      const tokenData = await Notifications.getExpoPushTokenAsync();
      const expoPushToken = tokenData.data;

      console.log(`[DEBUG] 6. Expo Token 발급 성공: ${expoPushToken}`);
      alert(`[DEBUG] 6. Expo Token 발급 성공: ${expoPushToken}`);

      if (webViewRef.current) {
        const script = `
          window.dispatchEvent(new CustomEvent('mobileNotificationPermission', {
            detail: { token: '${expoPushToken}', granted: true }
          }));
          true;
        `;
        webViewRef.current.injectJavaScript(script);
      }

      return expoPushToken;
    } catch (tokenError) {
      console.error("Expo Token 가져오기 실패:", tokenError);
      alert(`[ERROR] Expo Token 가져오기 실패: ${tokenError}`);
      // 토큰 가져오기는 실패해도 승인된 권한은 전달해줘야 됨
      if (webViewRef.current) {
        const script = `
          window.dispatchEvent(new CustomEvent('notificationPermission', {
            detail: { granted: true }
          }));
          true;
        `;
        webViewRef.current.injectJavaScript(script);
      }
      return null;
    }
  };

  return {
    handleNotificationPermission,
    getExpoPushToken,
  };
};
