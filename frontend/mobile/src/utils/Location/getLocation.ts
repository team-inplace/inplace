import * as Location from "expo-location";
import { WebView } from "react-native-webview";

const getLocation = async (
  webView: WebView,
  setGpsLoading: React.Dispatch<React.SetStateAction<boolean>>
) => {
  setGpsLoading(true);
  try {
    alert("[DEBUG] getCurrentPositionAsync 호출");
    const { coords } = await Location.getCurrentPositionAsync({
      accuracy: Location.Accuracy.High,
    });

    const location = {
      latitude: coords.latitude,
      longitude: coords.longitude,
    };

    alert(`[DEBUG] 위치 획득: ${JSON.stringify(location)}`);
    webView.postMessage(
      JSON.stringify({ type: "NATIVE_LOCATION", payload: location })
    );
    alert("[DEBUG] NATIVE_LOCATION postMessage 전송 완료");
  } catch (error) {
    alert(`[DEBUG] getLocation 에러: ${error}`);
    console.error("위치 정보를 가져오는 데 실패했습니다:", error);
  } finally {
    setGpsLoading(false);
  }
};

export default getLocation;
