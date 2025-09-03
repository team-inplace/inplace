import * as Location from "expo-location";
import { Alert, Linking } from "react-native";

const requestPermissions = async (
  setPermissionsGranted: React.Dispatch<React.SetStateAction<boolean | null>>
) => {
  const { status } = await Location.requestForegroundPermissionsAsync();
  console.log("Location Permission Status:", status);
  if (status !== "granted") {
    Alert.alert(
      "위치 정보 접근 거부",
      "위치 권한이 필요합니다.",
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
    setPermissionsGranted(false);
    return false;
  }
  setPermissionsGranted(true);
  return true;
};

export default requestPermissions;
