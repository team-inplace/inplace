// Import the functions you need from the SDKs you need
import { initializeApp } from 'firebase/app';
import { getMessaging, type Messaging } from 'firebase/messaging';
// TODO: Add SDKs for Firebase products that you want use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: import.meta.env.VITE_FIREBASE_API_KEY,
  authDomain: import.meta.env.VITE_FIREBASE_AUTH_DOMAIN,
  projectId: import.meta.env.VITE_FIREBASE_PROJECT_ID,
  storageBucket: import.meta.env.VITE_FIREBASE_STORAGE_BUCKET,
  messagingSenderId: import.meta.env.VITE_FIREBASE_MESSAGING_SENDER_ID,
  appId: import.meta.env.VITE_FIREBASE_APP_ID,
  measurementId: import.meta.env.VITE_FIREBASE_MEASUREMENT_ID,
};

// Initialize Firebase
export const app = initializeApp(firebaseConfig);

// Initialize messaging with proper type
const initializeMessaging = (): Messaging | null => {
  try {
    if (typeof window !== 'undefined' && !window.ReactNativeWebView) {
      return getMessaging(app);
    }
  } catch (error) {
    console.warn('Firebase messaging not supported in this environment:', error);
  }
  return null;
};

export const messaging = initializeMessaging();
