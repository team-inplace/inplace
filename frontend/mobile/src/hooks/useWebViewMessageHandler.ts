import { useCallback } from "react";

interface MessageHandlers {
  onGpsPermissionRequest: () => void;
  onLoginWithKakao: () => void;
  onRefreshToken: () => void;
  onLogout: () => void;
  onNotificationPermission?: () => Promise<void>;
}

export const useWebViewMessageHandler = (handlers: MessageHandlers) => {
  const handleMessage = useCallback(
    async (event: any) => {
      try {
        const message = JSON.parse(event.nativeEvent.data);

        switch (message.type) {
          case "GPS_PERMISSIONS":
            handlers.onGpsPermissionRequest();
            break;
          case "requestKakaoLogin":
            handlers.onLoginWithKakao();
            break;
          case "REQUEST_REFRESH_TOKEN":
            handlers.onRefreshToken();
            break;
          case "LOGOUT":
            handlers.onLogout();
            break;
          case "NOTIFICATION_PERMISSION":
            if (handlers.onNotificationPermission) {
              await handlers.onNotificationPermission();
            }
            break;
        }
      } catch (error) {
        console.error("메시지 처리 중 에러 발생", error);
      }
    },
    [handlers]
  );

  return { handleMessage };
};
