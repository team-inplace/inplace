import { useState } from "react";
import { Linking } from "react-native";
import { WebView } from "react-native-webview";

import checkLocation from "../utils/Location/checkLocation";
import requestPermissions from "../utils/Location/requestPermissions";
import getLocation from "../utils/Location/getLocation";

interface ModalContent {
  title: string;
  message: string;
  onConfirm: () => void;
}

export const useLocation = (webViewRef: React.RefObject<WebView | null>) => {
  const [modalVisible, setModalVisible] = useState(false);
  const [modalContent, setModalContent] = useState<ModalContent | null>(null);

  const showLocationModal = async () => {
    const servicesEnabled = await checkLocation();
    if (!servicesEnabled) {
      setModalContent({
        title: "위치 서비스 필요",
        message:
          '위치 서비스를 사용하려면\n"기기의 설정 > 개인 정보 보호"에서\n위치 서비스를 켜주세요.',
        onConfirm: () => Linking.openSettings(), // '설정으로 이동' 버튼
      });
      setModalVisible(true);
      return;
    }

    const permissionsGranted = await requestPermissions();
    if (!permissionsGranted) {
      setModalContent({
        title: "위치 정보 접근 권한 필요",
        message:
          "정확한 위치 정보를 제공하기 위해\n위치 정보 접근 권한이 필요합니다.",
        onConfirm: () => Linking.openSettings(), // '설정으로 이동' 버튼
      });
      setModalVisible(true);
      return;
    }

    if (webViewRef.current) {
      getLocation(webViewRef.current, () => {});
    } else {
      console.warn("WebView ref가 아직 설정되지 않았습니다.");
    }
  };

  const hideModal = () => {
    setModalVisible(false);
  };

  return {
    modalVisible,
    modalContent,
    showLocationModal,
    hideModal,
  };
};
