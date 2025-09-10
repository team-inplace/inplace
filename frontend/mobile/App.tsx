import { SafeAreaView, StyleSheet } from "react-native";
import CustomWebView from "./src/components/common/CustomWebview";
import { WEB_VIEW_URL } from "./src/utils/constants/webURL";
import { useRef, useState } from "react";
import { WebView, WebViewMessageEvent } from "react-native-webview";
import requestPermissions from "./src/utils/Location/requestPermissions";

export default function App() {
  const webViewRef = useRef<WebView | null>(null);
  const [permissionsGranted, setPermissionsGranted] = useState<boolean | null>(null);

  const handleMessage = async (event: WebViewMessageEvent) => {
    const message = JSON.parse(event.nativeEvent.data);

    if (message.type === "GPS_PERMISSIONS") {
      await requestPermissions(setPermissionsGranted);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <CustomWebView 
        ref={webViewRef}
        url={WEB_VIEW_URL} 
        onMessage={handleMessage}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});
