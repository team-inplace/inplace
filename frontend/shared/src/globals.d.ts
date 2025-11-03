declare const __DEV__: boolean;

interface Window {
  ReactNativeWebView?: {
    postMessage: (message: string) => void;
  };
}
