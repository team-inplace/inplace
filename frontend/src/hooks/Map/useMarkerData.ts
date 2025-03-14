import { useState, useEffect, useCallback, RefObject } from 'react';
import { MarkerInfo, PlaceData } from '@/types';
import { useGetMarkerInfo } from '@/api/hooks/useGetMarkerInfo';

interface UseMarkerDataProps {
  selectedPlaceId: number | null;
  placeData: PlaceData[];
  onPlaceSelect: (placeId: number | null) => void;
  moveMapToMarker: (lat: number, lng: number) => void;
  mapRef: RefObject<kakao.maps.Map | null>;
}

export default function useMarkerData({
  selectedPlaceId,
  placeData,
  onPlaceSelect,
  moveMapToMarker,
  mapRef,
}: UseMarkerDataProps) {
  const [markerInfo, setMarkerInfo] = useState<MarkerInfo | PlaceData | null>(null);
  const [shouldFetchData, setShouldFetchData] = useState(false);

  // 마커 데이터를 가져오는 API 호출
  const MarkerInfoData = useGetMarkerInfo(selectedPlaceId?.toString() || '', shouldFetchData);

  // API에서 데이터 받아오면 업데이트
  useEffect(() => {
    if (shouldFetchData && MarkerInfoData.data) {
      setMarkerInfo(MarkerInfoData.data);
      setShouldFetchData(false);
    }
  }, [MarkerInfoData.data, shouldFetchData]);

  // 선택된 장소가 있을 경우, 기존 데이터 활용 또는 새로 API 호출
  const getMarkerInfoWithPlaceInfo = useCallback(
    (placeId: number) => {
      if (!placeData) return;

      const existData = placeData.find((m) => m.placeId === placeId);
      if (existData) {
        setMarkerInfo(existData);
        setShouldFetchData(false);
      } else {
        setShouldFetchData(true);
      }
    },
    [placeData],
  );

  // 장소가 선택되었을 경우 마커 정보 불러오기
  useEffect(() => {
    if (selectedPlaceId) {
      getMarkerInfoWithPlaceInfo(selectedPlaceId);
    }
  }, [selectedPlaceId, placeData, getMarkerInfoWithPlaceInfo]);

  // 마커 클릭 시 처리
  const handleMarkerClick = useCallback(
    (placeId: number, marker: kakao.maps.Marker) => {
      if (mapRef.current && marker) {
        onPlaceSelect(selectedPlaceId === placeId ? null : placeId);
        if (selectedPlaceId !== placeId) {
          const pos = marker.getPosition();
          moveMapToMarker(pos.getLat(), pos.getLng());
        }
      }
    },
    [selectedPlaceId, onPlaceSelect, moveMapToMarker],
  );

  const handleMapClick = useCallback(() => {
    if (selectedPlaceId !== null) {
      onPlaceSelect(null);
    }
  }, [selectedPlaceId, onPlaceSelect]);

  return { markerInfo, handleMarkerClick, handleMapClick };
}
