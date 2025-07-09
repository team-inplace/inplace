import { useCallback, useRef } from 'react';

interface UseMapActionsProps {
  onPlaceSelect: (placeId: number | null) => void;
  mapRef: React.MutableRefObject<kakao.maps.Map | null>;
  isFromDetail?: boolean;
}

export default function useMapActions({ mapRef, onPlaceSelect, isFromDetail }: UseMapActionsProps) {
  const PAN_DELAY_MS = 100;
  const DEFAULT_ZOOM_LEVEL = 4;
  const MIN_ZOOM_LEVEL = 2;
  const BASE_OFFSET_Y = -0.002;
  const SMALL_ZOOM_OFFSET_Y = -0.0007;

  const restorationCompleteRef = useRef(false); // 복원 완료 시점 추적 용도

  const moveMapToMarker = useCallback((latitude: number, longitude: number, forceMove = false) => {
    if (!mapRef.current) return;
    if (isFromDetail && !restorationCompleteRef.current && !forceMove) {
      // 디테일 복원 중인 경우 지도 이동 skip (사용자 직접 클릭하는 경우는 예외)
      return;
    }
    const currentLevel = mapRef.current.getLevel();

    if (currentLevel === 1) {
      mapRef.current.setLevel(MIN_ZOOM_LEVEL, { animate: false });
    }

    if (currentLevel > DEFAULT_ZOOM_LEVEL) {
      mapRef.current.setLevel(DEFAULT_ZOOM_LEVEL, { animate: false });
    }

    const offsetY = currentLevel < DEFAULT_ZOOM_LEVEL ? SMALL_ZOOM_OFFSET_Y : BASE_OFFSET_Y;
    const position = new kakao.maps.LatLng(latitude - offsetY, longitude);
    setTimeout(() => {
      mapRef.current?.panTo(position);
    }, PAN_DELAY_MS);
  }, []);

  const handleCenterReset = useCallback(
    (userLocation: { lat: number; lng: number }) => {
      if (mapRef.current && userLocation) {
        mapRef.current.setCenter(new kakao.maps.LatLng(userLocation.lat, userLocation.lng));
        mapRef.current.setLevel(DEFAULT_ZOOM_LEVEL);
        onPlaceSelect(null);
      }
    },
    [onPlaceSelect],
  );

  const markRestorationComplete = useCallback(() => {
    restorationCompleteRef.current = true;
  }, []);

  return {
    moveMapToMarker,
    handleCenterReset,
    markRestorationComplete,
  };
}
