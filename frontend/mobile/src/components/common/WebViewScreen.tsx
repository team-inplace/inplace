import { useEffect, useRef, useState } from "react";
import { ActivityIndicator, SafeAreaView, StyleSheet } from "react-native";
import WebView from "react-native-webview";
import { useLocation } from "../../hooks/useLocation";
import CustomWebView from "./CustomWebview";
import LocationPermissionModal from "../location/LocationPermissionModal";
import { useWebViewMessageHandler } from "../../hooks/useWebViewMessageHandler";
import { useAuth } from "../../hooks/useAuth";
import { BASE_URL } from "@inplace-frontend-monorepo/shared/src/api/instance";

export default function WebViewScreen() {
  const webViewRef = useRef<WebView | null>(null);
  const [isWebViewReady, setWebViewReady] = useState(false);

  const { modalVisible, modalContent, showLocationModal, hideModal } =
    useLocation(webViewRef);
  const { handleKakaoLogin } = useAuth(webViewRef);
  const { handleMessage } = useWebViewMessageHandler({
    onGpsPermissionRequest: showLocationModal,
    onLoginWithKakao: handleKakaoLogin,
  });

  useEffect(() => {
    setWebViewReady(true);
  }, []);

  return (
    <SafeAreaView style={styles.container}>
      {isWebViewReady ? (
        <>
          <CustomWebView
            ref={webViewRef}
            url={BASE_URL}
            onMessage={handleMessage}
          />
          {modalContent && (
            <LocationPermissionModal
              visible={modalVisible}
              title={modalContent.title}
              message={modalContent.message}
              onConfirm={() => {
                modalContent.onConfirm();
                hideModal();
              }}
              onClose={hideModal}
            />
          )}
        </>
      ) : (
        <ActivityIndicator style={styles.loader} size="large" color="#0000ff" />
      )}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  loader: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
});
