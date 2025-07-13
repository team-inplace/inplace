import { StatusBar } from "expo-status-bar";
import { Platform, SafeAreaView, StyleSheet } from "react-native";
import WebView from "react-native-webview";

export default function App() {
  const webViewUrl = "https://www.inplace.my";

  return (
    <SafeAreaView style={styles.container}>
      <WebView
        source={{ uri: webViewUrl }}
        allowsbackforwardnavigationgestures={true}
        style={styles.webview}
      />
    </SafeAreaView>
  );
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
    // iOS에서 Safe Area를 처리 (노치 디자인 고려)
    paddingTop: Platform.OS === "android" ? 25 : 0,
  },
  webview: {
    flex: 1,
  },
});
