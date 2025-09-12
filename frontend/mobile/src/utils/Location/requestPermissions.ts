import * as Location from "expo-location";

const requestPermissions = async (): Promise<boolean> => {
  const { status } = await Location.requestForegroundPermissionsAsync();
  console.log("Location Permission Status:", status);
  return status === "granted";
};

export default requestPermissions;
