import React, { forwardRef } from "react";
import { WebView, WebViewMessageEvent } from "react-native-webview";
import { Platform, SafeAreaView, StyleSheet } from "react-native";

export interface CustomWebViewProps {
  url: string;
  onMessage?: (event: WebViewMessageEvent) => void;
}

const CustomWebView = forwardRef<WebView, CustomWebViewProps>(
  ({ url, onMessage }, ref) => {
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
