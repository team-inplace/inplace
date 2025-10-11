import * as Location from "expo-location";

const checkLocation = async (): Promise<boolean> => {
  const isEnabled = await Location.hasServicesEnabledAsync();
  return isEnabled;
};

export default checkLocation;
