import { useCallback, useEffect, useRef, useState } from 'react';
import styled from 'styled-components';
import { MarkerData } from '@/types';
import { useGetAllMarkers } from '@/api/hooks/useGetAllMarkers';
import InfluencerMapWindow from './InfluencerMapWindow';
import InfluencerPlaceSection from './InfluencerPlaceSection';
import useTouchDrag from '@/hooks/Map/useTouchDrag';
import useMapState from '@/hooks/Map/useMapState';

export default function InfluencerMapTap({
  influencerImg,
  influencerName,
}: {
  influencerImg: string;
  influencerName: string;
}) {
  const {
    center,
    mapBounds,
    setCenter,
    setMapBounds,
    isListExpanded,
    selectedPlaceId,
    placeData,
    setIsListExpanded,
    handlePlaceSelect,
    handleGetPlaceData,
  } = useMapState();
  const { translateY, setTranslateY, handleTouchStart, handleTouchMove, handleTouchEnd } =
    useTouchDrag(setIsListExpanded);
  const fetchLocationRef = useRef<() => void>();
  const filters = { categories: [], influencers: [influencerName], placeName: '' };
  const [shouldFetchPlaces, setShouldFetchPlaces] = useState(false);
  const [markers, setMarkers] = useState<MarkerData[]>([]);
  const [isInitialLoad, setIsInitialLoad] = useState(true);
  const { data: fetchedMarkers = [] } = useGetAllMarkers(
    {
      location: {
        topLeftLatitude: 40.96529356918684,
        topLeftLongitude: 117.35362493334182,
        bottomRightLatitude: 30.52810554762812,
        bottomRightLongitude: 139.31996436541462,
      },
      filters,
      center,
    },
    isInitialLoad,
  );
  useEffect(() => {
    if (isInitialLoad && fetchedMarkers.length > 0) {
      setMarkers(fetchedMarkers);
      setIsInitialLoad(false);
    }
  }, [isInitialLoad, fetchedMarkers]);

  const handleCompleteFetch = useCallback((value: boolean) => {
    setShouldFetchPlaces(value);
  }, []);

  const handleListExpand = useCallback((value: boolean) => {
    setIsListExpanded(value);
    setTranslateY(value ? 0 : window.innerHeight);
  }, []);

  const handleNearbySearch = useCallback((fn: () => void) => {
    fetchLocationRef.current = fn;
  }, []);

  return (
    <Wrapper>
      <InfluencerMapWindow
        influencerImg={influencerImg}
        placeData={placeData}
        setCenter={setCenter}
        setMapBounds={setMapBounds}
        markers={markers}
        selectedPlaceId={selectedPlaceId}
        // shouldFetchPlaces={shouldFetchPlaces}
        onCompleteFetch={handleCompleteFetch}
        onPlaceSelect={handlePlaceSelect}
        isListExpanded={isListExpanded}
        onListExpand={handleListExpand}
        onNearbySearch={handleNearbySearch}
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
          onPlaceSelect={handlePlaceSelect}
          selectedPlaceId={selectedPlaceId}
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
          onPlaceSelect={handlePlaceSelect}
          selectedPlaceId={selectedPlaceId}
          isInitialLoad={isInitialLoad}
          isListExpanded={isListExpanded}
          onListExpand={handleListExpand}
          onSearchNearby={() => fetchLocationRef.current?.()}
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
