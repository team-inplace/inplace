import { initializeConfig } from "@inplace-frontend-monorepo/shared/src/api/config/index";
initializeConfig(__DEV__ ? "development" : "production");

import WebViewScreen from "./src/components/common/WebViewScreen";

export default function App() {
  return <WebViewScreen />;
}
