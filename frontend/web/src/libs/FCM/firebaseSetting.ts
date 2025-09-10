// Import the functions you need from the SDKs you need
import { initializeApp } from 'firebase/app';
import { getMessaging, type Messaging } from 'firebase/messaging';
// TODO: Add SDKs for Firebase products that you want use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: 'AIzaSyDhN1r3xw82OiadDuKxv4i2c7JOLIseOiw',
  authDomain: 'inplace-70c36.firebaseapp.com',
  projectId: 'inplace-70c36',
  storageBucket: 'inplace-70c36.firebasestorage.app',
  messagingSenderId: '799415377379',
  appId: '1:799415377379:web:142590452f3452073114d6',
  measurementId: 'G-E5J7MJJV9P',
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
