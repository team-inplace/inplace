import { useCallback, useEffect, useRef, useState } from 'react';
import styled from 'styled-components';
import { MarkerData } from '@/types';
import { useGetAllMarkers } from '@/api/hooks/useGetAllMarkers';
import InfluencerMapWindow from './InfluencerMapWindow';
import InfluencerPlaceSection from './InfluencerPlaceSection';
import useTouchDrag from '@/hooks/Map/useTouchDrag';
import useMapState from '@/hooks/Map/useMapState';

interface StoredMapState {
  selectedPlaceId?: number | null;
  center?: { lat: number; lng: number };
  zoomLevel?: number;
}

export default function InfluencerMapTap({
  influencerImg,
  influencerName,
}: {
  influencerImg: string;
  influencerName: string;
}) {
  const getInitialMapState = (): { state: StoredMapState; isFromDetail: boolean } => {
    try {
      const isFromDetail = sessionStorage.getItem('fromDetail') === 'true';
      if (isFromDetail) {
        const stored = sessionStorage.getItem('influencerMap_state');
        if (stored) {
          const parsedData: StoredMapState = JSON.parse(stored);
          return { state: parsedData, isFromDetail: true };
        }
      }
    } catch (error) {
      console.error('InfluencerMapTap getInitialMapState 에러:', error);
    }

    return {
      state: { selectedPlaceId: null },
      isFromDetail: false,
    };
  };

  const { state: initialState, isFromDetail } = getInitialMapState();

  const [center, setCenter] = useState(initialState.center || { lat: 36.2683, lng: 127.6358 });
  const [mapBounds, setMapBounds] = useState({
    topLeftLatitude: 40.96529356918684,
    topLeftLongitude: 117.35362493334182,
    bottomRightLatitude: 30.52810554762812,
    bottomRightLongitude: 139.31996436541462,
  });
  const [savedZoomLevel, setSavedZoomLevel] = useState<number | undefined>(initialState.zoomLevel || 14);
  const [isRestoredFromDetail, setIsRestoredFromDetail] = useState(isFromDetail);
  const [initialSelectedPlaceId] = useState(initialState.selectedPlaceId);
  const [hasRestored, setHasRestored] = useState(false);
  const [shouldRestoreScroll, setShouldRestoreScroll] = useState(false);

  const {
    isListExpanded,
    selectedPlaceId,
    placeData,
    setIsListExpanded,
    handlePlaceSelect,
    forceSelectPlace,
    handleGetPlaceData,
  } = useMapState();
  const { translateY, setTranslateY, handleTouchStart, handleTouchMove, handleTouchEnd } =
    useTouchDrag(setIsListExpanded);
  const mapWindowNearbySearchRef = useRef<(() => void) | null>(null);
  const filters = { categories: [], influencers: [influencerName], placeName: '' };
  const [shouldFetchPlaces, setShouldFetchPlaces] = useState(false);
  const [markers, setMarkers] = useState<MarkerData[]>([]);
  const [isInitialLoad, setIsInitialLoad] = useState(!isFromDetail);
  const { data: fetchedMarkers = [] } = useGetAllMarkers(
    {
      location: mapBounds,
      filters,
      center,
    },
    true,
  );

  useEffect(() => {
    if (!isRestoredFromDetail) {
      const stateToStore = {
        selectedPlaceId,
        center,
        zoomLevel: savedZoomLevel,
      };
      sessionStorage.setItem('influencerMap_state', JSON.stringify(stateToStore));
    }
  }, [selectedPlaceId, center, savedZoomLevel, isRestoredFromDetail]);

  useEffect(() => {
    if (!isFromDetail) {
      sessionStorage.removeItem('influencerMap_state');
    }
  }, [isFromDetail]);

  useEffect(() => {
    if (fetchedMarkers.length > 0) {
      setMarkers(fetchedMarkers);
      setIsInitialLoad(false);
    }
  }, [fetchedMarkers]);

  // 마커 데이터 로딩 완료될 때까지 기다리지 않고, 바로 center을 복원
  useEffect(() => {
    if (isRestoredFromDetail && initialSelectedPlaceId && !hasRestored) {
      forceSelectPlace(initialSelectedPlaceId);
      setHasRestored(true);
    }
  }, [isRestoredFromDetail, initialSelectedPlaceId, hasRestored, forceSelectPlace]);

  useEffect(() => {
    if (isFromDetail) {
      const isMobile = window.innerWidth <= 768;

      if (isMobile) {
        setIsListExpanded(true);
        setTranslateY(0);
        setTimeout(() => {
          setShouldRestoreScroll(true);
        }, 400);
      } else {
        setTimeout(() => {
          setShouldRestoreScroll(true);
        }, 200);
      }
      setIsInitialLoad(false);
      setIsRestoredFromDetail(true);
      setTimeout(() => {
        sessionStorage.removeItem('fromDetail');
        setIsRestoredFromDetail(false);
      }, 3000);
    }
  }, [isFromDetail, setIsListExpanded, setTranslateY]);

  useEffect(() => {
    if (hasRestored && selectedPlaceId === initialSelectedPlaceId) {
      const timer = setTimeout(() => {
        setIsRestoredFromDetail(false);
        sessionStorage.removeItem('fromDetail');
      }, 1000);

      return () => clearTimeout(timer);
    }
    return undefined;
  }, [hasRestored, selectedPlaceId, initialSelectedPlaceId]);

  const handleCompleteFetch = useCallback((value: boolean) => {
    setShouldFetchPlaces(value);
  }, []);

  const handleListExpand = useCallback(
    (value: boolean) => {
      setIsListExpanded(value);
      setTranslateY(value ? 0 : window.innerHeight);
    },
    [setIsListExpanded, setTranslateY],
  );

  const onNearbySearchFromMapWindow = useCallback((fn: () => void) => {
    mapWindowNearbySearchRef.current = fn;
  }, []);

  const handleNearbySearchForMobile = useCallback(() => {
    mapWindowNearbySearchRef.current?.();
  }, []);

  const handlePlaceItemClick = useCallback(
    (placeId: number) => {
      handlePlaceSelect(placeId);
    },
    [handlePlaceSelect],
  );

  return (
    <Wrapper>
      <InfluencerMapWindow
        influencerImg={influencerImg}
        placeData={placeData}
        center={center}
        setCenter={setCenter}
        setMapBounds={setMapBounds}
        markers={markers}
        selectedPlaceId={selectedPlaceId}
        onCompleteFetch={handleCompleteFetch}
        onPlaceSelect={handlePlaceSelect}
        isListExpanded={isListExpanded}
        onListExpand={handleListExpand}
        onNearbySearch={onNearbySearchFromMapWindow}
        isRestoredFromDetail={isRestoredFromDetail}
        savedZoomLevel={savedZoomLevel}
        setSavedZoomLevel={setSavedZoomLevel}
        savedCenter={initialState.center}
      />
      <PlaceSectionDesktop>
        <InfluencerPlaceSection
          mapBounds={mapBounds}
          center={center}
          filters={filters}
          shouldFetchPlaces={shouldFetchPlaces}
          onCompleteFetch={handleCompleteFetch}
          onGetPlaceData={handleGetPlaceData}
          isInitialLoad={isInitialLoad}
          onPlaceSelect={handlePlaceItemClick}
          selectedPlaceId={selectedPlaceId}
          shouldRestoreScroll={shouldRestoreScroll}
          setShouldRestoreScroll={setShouldRestoreScroll}
        />
      </PlaceSectionDesktop>
      <MobilePlaceSection
        $isExpanded={isListExpanded}
        onClick={() => isListExpanded && setIsListExpanded(false)}
        $translateY={translateY}
        onTouchStart={handleTouchStart}
        onTouchMove={handleTouchMove}
        onTouchEnd={handleTouchEnd}
      >
        <DragHandle />
        <InfluencerPlaceSection
          mapBounds={mapBounds}
          center={center}
          filters={filters}
          shouldFetchPlaces={shouldFetchPlaces}
          onCompleteFetch={handleCompleteFetch}
          onGetPlaceData={handleGetPlaceData}
          onPlaceSelect={handlePlaceItemClick}
          selectedPlaceId={selectedPlaceId}
          isInitialLoad={isInitialLoad}
          isListExpanded={isListExpanded}
          onListExpand={handleListExpand}
          onSearchNearby={handleNearbySearchForMobile}
          shouldRestoreScroll={shouldRestoreScroll}
          setShouldRestoreScroll={setShouldRestoreScroll}
        />
      </MobilePlaceSection>
    </Wrapper>
  );
}
const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: end;
  touch-action: none;
`;
const PlaceSectionDesktop = styled.div`
  width: 100%;
  @media screen and (max-width: 768px) {
    display: none;
  }
`;

const MobilePlaceSection = styled.div<{ $translateY: number; $isExpanded: boolean }>`
  display: ${({ $isExpanded }) => ($isExpanded ? 'block' : 'none')};

  @media screen and (max-width: 768px) {
    display: block;
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    transform: translateY(${(props) => props.$translateY}px);
    height: 80vh;
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#3c3c3c' : '#fafafa')};
    z-index: 90;
    border-top-left-radius: 16px;
    border-top-right-radius: 16px;
    transition: transform 0.3s ease-out;
    touch-action: none;
  }
`;

const DragHandle = styled.div`
  width: 100%;
  height: 20px;
  padding: 12px 0;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  background-color: transparent;
  cursor: grab;
  touch-action: none;

  &:after {
    content: '';
    width: 40px;
    height: 4px;
    background-color: #666;
    border-radius: 2px;
  }
`;
