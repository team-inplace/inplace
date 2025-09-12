import * as Location from "expo-location";

const requestPermissions = async (): Promise<boolean> => {
  const { status } = await Location.requestForegroundPermissionsAsync();
  return status === "granted";
};

export default requestPermissions;
