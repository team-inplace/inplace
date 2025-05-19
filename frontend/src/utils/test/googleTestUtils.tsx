export type ABTestGroup = 'A' | 'B';

export const getCookie = (name: string): string | null => {
  const cookieName = `${name}=`;
  const cookies = document.cookie.split(';');

  for (let i = 0; i < cookies.length; i += 1) {
    const cookie = cookies[i].trim();
    if (cookie.indexOf(cookieName) === 0) {
      return cookie.substring(cookieName.length, cookie.length);
    }
  }

  return null;
};

export const setCookie = (name: string, value: string, days: number): void => {
  const date = new Date();
  date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
  const expires = `expires=${date.toUTCString()}`;
  document.cookie = `${name}=${value};${expires};path=/`;
};

interface DataLayerEvent {
  event: string;
  [key: string]: unknown;
}

// GA4 이벤트 전송 함수
export const sendGAEvent = (eventName: string, parameters: Record<string, unknown> = {}): void => {
  if (typeof window !== 'undefined') {
    window.dataLayer = window.dataLayer || [];

    window.dataLayer.push({
      event: eventName,
      ...parameters,
    });
  } else {
    console.warn('Google Analytics not initialized');
  }
};

declare global {
  interface Window {
    dataLayer: DataLayerEvent[];
    gtag?: (command: string, action: string, params?: Record<string, unknown>) => void;
  }
}
