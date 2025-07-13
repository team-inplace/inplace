import { useState, useEffect, useCallback } from 'react';
import { MarkerInfo, PlaceData } from '@/types';
import { useGetMarkerInfo } from '@/api/hooks/useGetMarkerInfo';

interface UseMarkerDataProps {
  selectedPlaceId: number | null;
  placeData: PlaceData[];
  onPlaceSelect: (placeId: number | null) => void;
  moveMapToMarker: (lat: number, lng: number) => void;
  mapRef: React.MutableRefObject<kakao.maps.Map | null>;
  isRestoredFromDetail?: boolean;
}

export default function useMarkerData({
  selectedPlaceId,
  placeData,
  onPlaceSelect,
  moveMapToMarker,
  mapRef,
  isRestoredFromDetail = false,
}: UseMarkerDataProps) {
  const [markerInfo, setMarkerInfo] = useState<MarkerInfo | PlaceData | null>(null);
  const [shouldFetchData, setShouldFetchData] = useState(false);
  const isValidPlaceId = selectedPlaceId !== null && selectedPlaceId > 0;

  // 마커 데이터를 가져오는 API 호출(복원 중에는 API 호출 방지)
  const MarkerInfoData = useGetMarkerInfo(
    isValidPlaceId ? selectedPlaceId.toString() : '',
    shouldFetchData && isValidPlaceId && !isRestoredFromDetail,
  );

  // API에서 데이터 받아오면 업데이트
  useEffect(() => {
    if (shouldFetchData && isValidPlaceId && MarkerInfoData.data && !isRestoredFromDetail) {
      setMarkerInfo(MarkerInfoData.data);
      setShouldFetchData(false);
    }
  }, [MarkerInfoData.data, shouldFetchData, isValidPlaceId, isRestoredFromDetail]);

  // 선택된 장소가 있을 경우, 기존 데이터 활용 또는 새로 API 호출
  const getMarkerInfoWithPlaceInfo = useCallback(
    (placeId: number) => {
      if (!placeData || placeData.length === 0) {
        if (!isRestoredFromDetail) {
          setShouldFetchData(true);
        }
        return;
      }

      const existData = placeData.find((m) => m.placeId === placeId);
      if (existData) {
        setMarkerInfo(existData);
        setShouldFetchData(false);
      } else if (!isRestoredFromDetail) {
        setShouldFetchData(true); // 복원 중이 아닐 때만 API 호출
      }
    },
    [placeData, isRestoredFromDetail],
  );

  // 장소가 선택되었을 경우 마커 정보 불러오기
  useEffect(() => {
    if (isValidPlaceId) {
      getMarkerInfoWithPlaceInfo(selectedPlaceId);
    } else {
      setMarkerInfo(null);
      setShouldFetchData(false);
    }
  }, [selectedPlaceId, isValidPlaceId, getMarkerInfoWithPlaceInfo]);

  // 복원히는 경우엔 api호출 안 하고 기존 데이터로 마커 정보 설정
  useEffect(() => {
    if (isRestoredFromDetail && isValidPlaceId && placeData.length > 0 && !markerInfo) {
      const existData = placeData.find((m) => m.placeId === selectedPlaceId);
      if (existData) {
        setMarkerInfo(existData);
      }
    }
  }, [isRestoredFromDetail, isValidPlaceId, selectedPlaceId, placeData, markerInfo]);

  // 마커 클릭 시 처리
  const handleMarkerClick = useCallback(
    (placeId: number, marker: kakao.maps.Marker) => {
      if (mapRef.current && marker) {
        onPlaceSelect(selectedPlaceId === placeId ? null : placeId);
        if (selectedPlaceId !== placeId) {
          const pos = marker.getPosition();
          // 사용자가 직접 클릭한 경우에는 항상 지도 이동
          moveMapToMarker(pos.getLat(), pos.getLng());
        }
      }
    },
    [selectedPlaceId, onPlaceSelect, moveMapToMarker, mapRef],
  );

  const handleMapClick = useCallback(() => {
    if (selectedPlaceId !== null) {
      onPlaceSelect(null);
    }
  }, [selectedPlaceId, onPlaceSelect]);

  return { markerInfo, handleMarkerClick, handleMapClick };
}
