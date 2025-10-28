import React, { forwardRef, useEffect, useState } from "react";
import { WebView, WebViewMessageEvent } from "react-native-webview";
import { BackHandler, Platform, SafeAreaView, StyleSheet } from "react-native";

export interface CustomWebViewProps {
  url: string;
  onMessage?: (event: WebViewMessageEvent) => void;
}

const CustomWebView = forwardRef<WebView, CustomWebViewProps>(
  ({ url, onMessage }, ref) => {
    const [backPressedOnce, setBackPressedOnce] = useState(false);

    useEffect(() => {
      const backAction = () => {
        if (backPressedOnce) {
          BackHandler.exitApp();
          return true; // 기본 동작은 막음 (중복 종료 방지)
        }
        setBackPressedOnce(true);
        setTimeout(() => {
          setBackPressedOnce(false);
        }, 2000);

        return true; // 기본 동작 막음 (아직 앱 종료 안 함)
      };
      
      const backHandler = BackHandler.addEventListener(
        "hardwareBackPress",
        backAction
      )
      return () => backHandler.remove();
    }, [backPressedOnce]);

    return (
      <SafeAreaView style={styles.container}>
        <WebView
          source={{ uri: url }}
          onMessage={onMessage}
          allowsBackForwardNavigationGestures={true}
          style={styles.webview}
          ref={ref}
        />
      </SafeAreaView>
    );
  }
);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingTop: Platform.OS === "android" ? 25 : 0,
  },
  webview: {
    flex: 1,
  },
});

export default CustomWebView;
