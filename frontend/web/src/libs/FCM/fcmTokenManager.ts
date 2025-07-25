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
  // 권한이 허용된 후에 토큰 발급
  const token = await getToken(messaging, {
    vapidKey: import.meta.env.VITE_VAPID_KEY,
  });
  return token;
}

export async function requestNotificationPermission() {
  try {
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
