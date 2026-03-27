import { useState, useEffect } from 'react';

export default function useGetLocation(enable: boolean) {
  const GEOLOCATION_CONFIG = {
    maximumAge: 500000,
    timeout: 50000,
  };
  const isReactNativeWebView = typeof window !== 'undefined' && window.ReactNativeWebView != null;

  const [location, setLocation] = useState<{ lat: number; lng: number } | null>(null);

  useEffect(() => {
    if (isReactNativeWebView) {
      window.ReactNativeWebView?.postMessage(JSON.stringify({ type: 'GPS_PERMISSIONS' }));

      const handleMessage = (event: MessageEvent) => {
        try {
          const message = JSON.parse(event.data);
          if (message.type === 'NATIVE_LOCATION' && message.payload?.latitude && message.payload?.longitude) {
            setLocation({
              lat: message.payload.latitude,
              lng: message.payload.longitude,
            });
          }
        } catch (e) {
          // ignore parse errors
        }
      };

      window.addEventListener('message', handleMessage);
      return () => window.removeEventListener('message', handleMessage);
    }
    if (!enable) {
      return undefined;
    }
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const newCenter = {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          };
          setLocation(newCenter);
        },
        (error) => {
          console.error('Error fetching location', error);
        },
        GEOLOCATION_CONFIG,
      );
    } else {
      console.error('Geolocation is not supported by this browser.');
    }
    return undefined;
  }, [enable]);

  return location;
}
