import { useState, useEffect } from 'react';

type LocationPermission = 'granted' | 'denied' | 'default';
interface AllPermissions {
  location: LocationPermission;
  notification: NotificationPermission;
  isLoading: boolean;
}

export default function usePermissions() {
  const [permissions, setPermissions] = useState<AllPermissions>({
    location: 'default',
    notification: 'default',
    isLoading: true,
  });

  useEffect(() => {
    const checkPermissions = async () => {
      try {
        const notificationPermission = Notification.permission; // 바로 접근
        let locationPermission: LocationPermission = 'default'; // 직접 접근 불가 -> api 조회 후 상태 업데이트 (let 사용)

        if ('geolocation' in navigator && 'permissions' in navigator) {
          // 위치 및 권한 API를 모두 지원하는지 확인
          try {
            const result = await navigator.permissions.query({ name: 'geolocation' }); // query 메서드는 현재 권한 상태 조회
            locationPermission = result.state as LocationPermission;
            console.log('위치 권한 상태:', locationPermission);
          } catch (error) {
            locationPermission = 'default';
          }
        }

        setPermissions({
          location: locationPermission,
          notification: notificationPermission,
          isLoading: false,
        });
      } catch (error) {
        console.error('권한 확인 중 오류:', error);
        setPermissions({
          location: 'default',
          notification: 'default',
          isLoading: false,
        });
      }
    };

    checkPermissions();
  }, []);

  const hasUndecidedPermissions = () => {
    return permissions.location === 'default' || permissions.notification === 'default';
  };

  return {
    permissions,
    hasUndecidedPermissions,
    isLoading: permissions.isLoading,
  };
}
