import { useState, useCallback } from 'react';
import { LocationData, PlaceData } from '@/types';

export default function useMapState() {
  const getInitialMapState = () => {
    try {
      const isFromDetail = sessionStorage.getItem('fromDetail') === 'true';
      const stored = sessionStorage.getItem('mapPage_state');
      if (isFromDetail && stored) {
        const parsedData = JSON.parse(stored);
        if (parsedData.center && parsedData.mapBounds) {
          return {
            center: parsedData.center,
            mapBounds: parsedData.mapBounds,
            isFromDetail: true,
          };
        }
      }
    } catch (error) {
      console.error('useMapState getInitialMapState 에러:', error);
    }
    const defaultState = {
      center: { lat: 37.5665, lng: 126.978 },
      mapBounds: {
        topLeftLatitude: 0,
        topLeftLongitude: 0,
        bottomRightLatitude: 0,
        bottomRightLongitude: 0,
      },
      isFromDetail: false,
    };
    return defaultState;
  };

  const initialState = getInitialMapState();
  const [center, setCenter] = useState(initialState.center);
  const [mapBounds, setMapBounds] = useState<LocationData>(initialState.mapBounds);
  const [isListExpanded, setIsListExpanded] = useState(false);
  const [selectedPlaceId, setSelectedPlaceId] = useState<number | null>(null);
  const [placeData, setPlaceData] = useState<PlaceData[]>([]);

  const handlePlaceSelect = useCallback((placeId: number | null) => {
    setSelectedPlaceId((prev) => (prev === placeId ? null : placeId));
  }, []);

  const forceSelectPlace = useCallback((placeId: number | null) => {
    setSelectedPlaceId(placeId);
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
    setMapBounds,
    mapBounds,
    isListExpanded,
    selectedPlaceId,
    placeData,
    setIsListExpanded,
    handlePlaceSelect,
    forceSelectPlace,
    handleGetPlaceData,
  };
}
