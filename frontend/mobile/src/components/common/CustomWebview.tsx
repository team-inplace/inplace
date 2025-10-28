import React, { forwardRef, useEffect, useState, useRef } from "react";
import { WebView, WebViewMessageEvent } from "react-native-webview";
import { BackHandler, Platform, SafeAreaView, StyleSheet } from "react-native";

export interface CustomWebViewProps {
  url: string;
  onMessage?: (event: WebViewMessageEvent) => void;
}

const CustomWebView = forwardRef<WebView, CustomWebViewProps>(
  ({ url, onMessage }, ref) => {
    const [canGoBack, setCanGoBack] = useState(false);
    const webViewRef = useRef<WebView | null>(null);

    useEffect(() => {
      const backAction = () => {
        if (canGoBack && webViewRef.current) {
          webViewRef.current.goBack();
          return true;
        }
        return false; // 뒤로 갈 곳 없으면 앱 종료
      };
      
      const backHandler = BackHandler.addEventListener(
        "hardwareBackPress",
        backAction
      );
      return () => backHandler.remove();
    }, [canGoBack]);

    return (
      <SafeAreaView style={styles.container}>
        <WebView
          ref={(webView) => {
            webViewRef.current = webView;
            if (typeof ref === 'function') {
              ref(webView);
            } else if (ref) {
              ref.current = webView;
            }
          }}
          source={{ uri: url }}
          onNavigationStateChange={(navState) => {
            setCanGoBack(navState.canGoBack);
          }}
          onMessage={onMessage}
          allowsBackForwardNavigationGestures={true}
          style={styles.webview}
          javaScriptEnabled={true}
          domStorageEnabled={true}
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