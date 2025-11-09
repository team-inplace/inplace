import { getToken } from 'firebase/messaging';
import { messaging } from './firebaseSetting';

// 서비스 워커 실행 함수
async function registerServiceWorker() {
  try {
    const registration = await navigator.serviceWorker.register('/firebase-messaging-sw.js');
    return registration;
  } catch (error) {
    console.error('Service Worker 등록 실패:', error);
    throw error;
  }
}

async function getDeviceToken() {
  if (!messaging) {
    throw new Error('Firebase messaging is not initialized');
  }
  // 권한이 허용된 후에 토큰 발급
  const token = await getToken(messaging, {
    vapidKey: import.meta.env.VITE_VAPID_KEY,
  });
  return token;
}
const isReactNativeWebView = typeof window !== 'undefined' && window.ReactNativeWebView != null;

export async function requestNotificationPermission() {
  try {
    if (isReactNativeWebView) {
      window.ReactNativeWebView?.postMessage(JSON.stringify({ type: 'NOTIFICATION_PERMISSION' }));
      return true; // 모바일에서는 메시지 전송 성공을 의미 (실제 권한 결과는 react native에서 처리)
    }
    const { permission } = Notification;
    let currentPermission = permission;
    if (currentPermission === 'default') {
      currentPermission = await Notification.requestPermission();
    }
    if (currentPermission === 'granted') {
      return true;
    }
    return false;
  } catch (error) {
    console.error('알림 권한 요청 중 오류 발생:', error);
    return false;
  }
}

export async function setupFCMToken(postDeviceToken: (token: string) => Promise<void>) {
  try {
    if (!messaging || (typeof window !== 'undefined' && window.ReactNativeWebView)) {
      console.log('FCM not available in this environment');
      return null;
    }

    if (Notification.permission !== 'granted') {
      return null;
    }
    await registerServiceWorker();
    const token = await getDeviceToken();
    await postDeviceToken(token);
    return token;
  } catch (error) {
    console.error('FCM 토큰 설정 중 오류 발생:', error);
    throw error;
  }
}
