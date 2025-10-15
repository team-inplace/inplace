import * as Location from "expo-location";
import { WebView } from "react-native-webview";

const getLocation = async (
  webView: WebView,
  setGpsLoading: React.Dispatch<React.SetStateAction<boolean>>
) => {
  setGpsLoading(true);
  try {
    const { coords } = await Location.getCurrentPositionAsync({
      accuracy: Location.Accuracy.High,
    });

    const location = {
      latitude: coords.latitude,
      longitude: coords.longitude,
    };

    webView.postMessage(
      JSON.stringify({ type: "NATIVE_LOCATION", payload: location })
    );
  } catch (error) {
    console.error("위치 정보를 가져오는 데 실패했습니다:", error);
  } finally {
    setGpsLoading(false);
  }
};

export default getLocation;
