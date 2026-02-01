import { initializeConfig } from "@inplace-frontend-monorepo/shared/src/api/config/index";

const environment = process.env.EXPO_PUBLIC_ENV === "development"
  ? "development"
  : process.env.EXPO_PUBLIC_ENV === "production"
  ? "production"
  : __DEV__ ? "development" : "production";

initializeConfig(environment);

import WebViewScreen from "./src/components/common/WebViewScreen";

export default function App() {
  return <WebViewScreen />;
}
