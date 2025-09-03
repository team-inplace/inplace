import { SafeAreaView } from "react-native";
import WebView, { WebViewMessageEvent } from "react-native-webview";
import CustomWebView from "../common/CustomWebview";
import { WEB_VIEW_URL } from "../../utils/constants/webURL";
import { useRef, useState } from "react";
import checkLocation from "../../utils/Location/checkLocation";
import requestPermissions from "../../utils/Location/requestPermissions";
import getLocation from "../../utils/Location/getLocation";

export default function GetLocation() {
  const webViewRef = useRef<WebView | null>(null);
  const [gpsLoading, setGpsLoading] = useState(false);
  const [permissionsGranted, setPermissionsGranted] = useState<boolean | null>(
    null
  );

  const handleMessage = async (event: WebViewMessageEvent) => {
    const message = JSON.parse(event.nativeEvent.data);

    if (message.type === "GPS_PERMISSIONS") {
      const servicesEnabled = await checkLocation();
      if (!servicesEnabled) return;

      if (permissionsGranted === null) {
        const permissionsGranted = await requestPermissions(
          setPermissionsGranted
        );
        if (!permissionsGranted) return;
      }
      getLocation(webViewRef, setGpsLoading);
    }
  };

  return (
    <CustomWebView
      ref={webViewRef}
      url={WEB_VIEW_URL}
      onMessage={handleMessage}
    />
  );
}
