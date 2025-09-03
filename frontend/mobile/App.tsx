import { SafeAreaView, StyleSheet } from "react-native";
import CustomWebView from "./src/components/common/CustomWebview";
import { WEB_VIEW_URL } from "./src/utils/constants/webURL";

export default function App() {
  return (
    <SafeAreaView style={styles.container}>
      <CustomWebView url={WEB_VIEW_URL} />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});
