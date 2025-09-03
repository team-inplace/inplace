import * as Location from "expo-location";
import React from "react";
import WebView from "react-native-webview";

const getLocation = async (
  webViewRef: React.RefObject<WebView>,
  setGpsLoading: React.Dispatch<React.SetStateAction<boolean>>
) => {
  try {
    setGpsLoading(true);

    const locationSubscription = await Location.watchPositionAsync(
      {
        distanceInterval: 1,
        accuracy: Location.Accuracy.High,
      },
      (location) => {
        const { latitude, longitude } = location.coords;

        // WebView로 좌표 정보 전송
        if (webViewRef.current) {
          webViewRef.current.postMessage(
            JSON.stringify({ latitude, longitude })
          );
        }
      }
    );
    // 위치 추적을 취소할 수 있는 객체 반환
    return locationSubscription;
  } catch (error) {
    console.error("Error getting location:", error);
  } finally {
    setGpsLoading(false);
  }
};
export default getLocation;
