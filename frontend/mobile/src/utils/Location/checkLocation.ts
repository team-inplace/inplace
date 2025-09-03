import * as Location from "expo-location";
import { Alert, Linking } from "react-native";

const checkLocation = async () => {
  const isEnabled = await Location.hasServicesEnabledAsync();
  if (!isEnabled) {
    Alert.alert(
      "위치 서비스 사용",
      '위치 서비스를 사용할 수 없습니다. "기기의 설정 > 개인 정보 보호" 에서 위치서비스를 켜주세요.',
      [
        { text: "취소", style: "cancel" },
        {
          text: "설정으로 이동",
          onPress: () => {
            Linking.openSettings();
          },
        },
      ],
      { cancelable: false }
    );
    return false;
  }
  return true;
};

export default checkLocation;
