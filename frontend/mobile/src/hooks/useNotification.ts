import { Platform } from "react-native";
import WebView from "react-native-webview";
import * as Notifications from "expo-notifications";

export const useNotification = (webViewRef: React.RefObject<WebView | null>) => {
  const handleNotificationPermission = async (): Promise<void> => {
    try {
      const { status: existingStatus } =
        await Notifications.getPermissionsAsync();
      
      const finalStatus = existingStatus !== "granted" // 권한이 없으면 요청(없거나 거부된 상태)
        ? (await Notifications.requestPermissionsAsync()).status
        : existingStatus;

      if (finalStatus !== "granted") {
        console.log("알림 권한이 거부되었습니다");
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
      await getExpoPushToken();
    } catch (error) {
      console.error("알림 권한 요청 실패:", error);
    }
  };

  const getExpoPushToken = async () => {
    try {
      const tokenData = await Notifications.getExpoPushTokenAsync({
        projectId: process.env.EXPO_PUBLIC_PROJECT_ID,
      });
      const expoPushToken = tokenData.data;

      if (webViewRef.current) {
        const script = `
          window.dispatchEvent(new CustomEvent('notificationPermission', {
            detail: { token: '${expoPushToken}', granted: true }
          }));
          true;
        `;
        webViewRef.current.injectJavaScript(script);
      }

      return expoPushToken;
    } catch (tokenError) {
      console.error("Expo Token 가져오기 실패:", tokenError);
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
