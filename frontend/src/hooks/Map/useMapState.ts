import { useState, useCallback } from 'react';
import { LocationData, PlaceData } from '@/types';

export default function useMapState() {
  const [center, setCenter] = useState({ lat: 37.5665, lng: 126.978 });
  const [mapBounds, setMapBounds] = useState<LocationData>({
    topLeftLatitude: 0,
    topLeftLongitude: 0,
    bottomRightLatitude: 0,
    bottomRightLongitude: 0,
  });
  const [isListExpanded, setIsListExpanded] = useState(false);
  const [selectedPlaceId, setSelectedPlaceId] = useState<number | null>(null);
  const [placeData, setPlaceData] = useState<PlaceData[]>([]);

  const handleBoundsChange = useCallback((bounds: LocationData) => {
    setMapBounds(bounds);
  }, []);

  const handleCenterChange = useCallback((newCenter: { lat: number; lng: number }) => {
    setCenter(newCenter);
  }, []);

  const handlePlaceSelect = useCallback((placeId: number | null) => {
    setSelectedPlaceId((prev) => (prev === placeId ? null : placeId));
  }, []);

  const handleGetPlaceData = useCallback((data: PlaceData[]) => {
    setPlaceData((prevData) => {
      if (JSON.stringify(prevData) !== JSON.stringify(data)) {
        return data;
      }
      return prevData;
    });
  }, []);

  return {
    center,
    setCenter,
    mapBounds,
    isListExpanded,
    selectedPlaceId,
    placeData,
    setIsListExpanded,
    handleBoundsChange,
    handleCenterChange,
    handlePlaceSelect,
    handleGetPlaceData,
  };
}
