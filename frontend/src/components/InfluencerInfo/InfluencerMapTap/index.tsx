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
}

export default function InfluencerMapTap({
  influencerImg,
  influencerName,
}: {
  influencerImg: string;
  influencerName: string;
}) {
  const getInitialMapState = (): StoredMapState => {
    try {
      const isFromDetail = sessionStorage.getItem('fromDetail') === 'true';
      if (isFromDetail) {
        const stored = sessionStorage.getItem('influencerMap_state');
        if (stored) {
          const parsedData: StoredMapState = JSON.parse(stored);
          return parsedData;
        }
      }
    } catch (error) {
      console.error('InfluencerMapTap getInitialMapState 에러:', error);
    }

    return {
      selectedPlaceId: null,
    };
  };

  const initialState = getInitialMapState();
  const isFromDetail = sessionStorage.getItem('fromDetail') === 'true';

  const [center, setCenter] = useState({ lat: 36.2683, lng: 127.6358 });
  const [mapBounds, setMapBounds] = useState({
    topLeftLatitude: 40.96529356918684,
    topLeftLongitude: 117.35362493334182,
    bottomRightLatitude: 30.52810554762812,
    bottomRightLongitude: 139.31996436541462,
  });
  const [savedZoomLevel, setSavedZoomLevel] = useState<number | undefined>(14);
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
    if (isRestoredFromDetail) {
      return;
    }
    const stateToStore = {
      selectedPlaceId,
    };
    sessionStorage.setItem('influencerMap_state', JSON.stringify(stateToStore));
  }, [selectedPlaceId, isRestoredFromDetail]);

  useEffect(() => {
    if (!isFromDetail) {
      sessionStorage.removeItem('influencerMap_state');
    }
  }, []);

  useEffect(() => {
    if (fetchedMarkers.length > 0) {
      setMarkers(fetchedMarkers);
      setIsInitialLoad(false);
    }
  }, [fetchedMarkers]);

  // placeData가 준비된 후에 selectedPlaceId 복원해야 됨
  useEffect(() => {
    if (isRestoredFromDetail && initialSelectedPlaceId && placeData.length > 0 && !hasRestored) {
      const targetPlace = placeData.find((place) => place.placeId === initialSelectedPlaceId);
      if (targetPlace) {
        forceSelectPlace(initialSelectedPlaceId);
        setHasRestored(true);
      }
    }
  }, [isRestoredFromDetail, initialSelectedPlaceId, placeData, hasRestored, forceSelectPlace]);

  useEffect(() => {
    if (sessionStorage.getItem('fromDetail') === 'true') {
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
        setIsRestoredFromDetail(false);
      }, 3000);
      sessionStorage.removeItem('fromDetail');
    }
  }, [setIsListExpanded, setTranslateY]);

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
