import { useCallback } from 'react';

export default function useMapActions(mapRef: React.MutableRefObject<kakao.maps.Map | null>) {
  const moveMapToMarker = useCallback(
    (latitude: number, longitude: number) => {
      if (!mapRef.current) return;

      const baseOffset = -0.007;
      const levelMultiplier = mapRef.current.getLevel() > 10 ? 2 : 1;
      const offsetY = (baseOffset * levelMultiplier) / 3;
      const position = new kakao.maps.LatLng(latitude - offsetY, longitude);

      if (mapRef.current.getLevel() > 4) {
        mapRef.current.setLevel(4, { anchor: position, animate: true });
      }

      setTimeout(() => mapRef.current?.panTo(position), 100);
    },
    [mapRef.current],
  );

  const handleResetCenter = useCallback((userLocation: { lat: number; lng: number }) => {
    if (mapRef.current && userLocation) {
      mapRef.current.setCenter(new kakao.maps.LatLng(userLocation.lat, userLocation.lng));
      mapRef.current.setLevel(4);
    }
  }, []);

  return { moveMapToMarker, handleResetCenter };
}
