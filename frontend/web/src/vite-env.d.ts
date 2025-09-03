/// <reference types="vite/client" />

interface Window {
  ReactNativeWebView?: {
    postMessage: (message: string) => void;
  };
}
