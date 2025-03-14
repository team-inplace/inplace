import { useEffect, useCallback, useRef } from 'react';
import styled from 'styled-components';
import { useInView } from 'react-intersection-observer';
import { GrPowerCycle } from 'react-icons/gr';
import PlaceItem from '@/components/Map/PlaceSection/PlaceItem';
import { PlaceData, LocationData } from '@/types';
import Loading from '@/components/common/layouts/Loading';
import NoItem from '@/components/common/layouts/NoItem';
import { useGetInfinitePlaceList } from '@/api/hooks/useGetInfinitePlaceList';
import usePlaceList from '@/hooks/Map/usePlaceList';

export interface PlaceSectionProps {
  mapBounds: LocationData;
  filters: {
    categories: string[];
    influencers: string[];
  };
  center: { lat: number; lng: number };
  shouldFetchPlaces: boolean;
  onCompleteFetch: (value: boolean) => void;
  onGetPlaceData: (data: PlaceData[]) => void;
  onPlaceSelect: (placeId: number) => void;
  selectedPlaceId: number | null;
  isListExpanded?: boolean;
  onListExpand?: (value: boolean) => void;
  onSearchNearby?: () => void;
}

export default function InfluencerPlaceSection({
  mapBounds,
  filters,
  center,
  shouldFetchPlaces,
  onCompleteFetch,
  onGetPlaceData,
  onPlaceSelect,
  selectedPlaceId,
  isListExpanded,
  onListExpand,
  onSearchNearby,
}: PlaceSectionProps) {
  const sectionRef = useRef<HTMLDivElement>(null);
  const previousPlacesRef = useRef<PlaceData[]>([]);

  const { ref: loadMoreRef, inView } = useInView({
    root: sectionRef.current,
    rootMargin: '0px',
    threshold: 0,
  });

  const { data, isLoading, isError, error, fetchNextPage, hasNextPage, isFetchingNextPage } = useGetInfinitePlaceList(
    {
      location: mapBounds,
      filters,
      center,
      size: 10,
    },
    shouldFetchPlaces,
  );

  const { filteredPlaces } = usePlaceList({ data, onGetPlaceData });

  useEffect(() => {
    if (shouldFetchPlaces) {
      onCompleteFetch(false);
    }
  }, [filteredPlaces, shouldFetchPlaces, onCompleteFetch, onGetPlaceData]);

  useEffect(() => {
    if (inView && hasNextPage && !isFetchingNextPage) {
      fetchNextPage();
    }
  }, [inView, hasNextPage, isFetchingNextPage, fetchNextPage]);

  // 장소 클릭 시 상위로 선택된 장소 전달
  const handlePlaceClick = useCallback(
    (placeId: number) => {
      onPlaceSelect(placeId);
      if (window.innerWidth <= 768 && onListExpand) {
        onListExpand(false);
      }
    },
    [onPlaceSelect, isListExpanded, onListExpand],
  );

  if (isLoading && !isFetchingNextPage && previousPlacesRef.current.length === 0) {
    return (
      <SectionContainer>
        <LoadingContainer>
          <Loading size={50} />
        </LoadingContainer>
      </SectionContainer>
    );
  }

  if (isError) {
    return (
      <SectionContainer>
        <ErrorContainer>Error: {(error as Error).message}</ErrorContainer>
      </SectionContainer>
    );
  }

  return (
    <SectionContainer ref={sectionRef}>
      {filteredPlaces.length === 0 ? (
        <>
          <ContentContainer>
            <Btn onClick={onSearchNearby}>
              <GrPowerCycle />
              현재 위치에서 장소정보 보기
            </Btn>
          </ContentContainer>
          <NoItem message="장소 정보가 없어요!" height={400} />
        </>
      ) : (
        <ContentContainer>
          <Btn onClick={onSearchNearby}>
            <GrPowerCycle />
            현재 위치에서 장소정보 보기
          </Btn>
          <PlacesGrid>
            {filteredPlaces.map((place) => (
              <PlaceItem
                key={place.placeId}
                {...place}
                onClick={() => handlePlaceClick(place.placeId)}
                isSelected={selectedPlaceId === place.placeId}
              />
            ))}
          </PlacesGrid>
          {(hasNextPage || isFetchingNextPage) && (
            <LoadMoreTrigger ref={loadMoreRef}>
              <Loading size={30} />
            </LoadMoreTrigger>
          )}
        </ContentContainer>
      )}
    </SectionContainer>
  );
}

const SectionContainer = styled.div`
  height: 600px;
  width: 100%;
  overflow-y: auto;
  padding-right: 4px;
  box-sizing: content-box;
  &::-webkit-scrollbar {
    width: 8px;
  }

  &::-webkit-scrollbar-thumb {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#1f1f1f' : '#8e8e8e')};
    border-radius: 4px;
    border: none;
  }

  &::-webkit-scrollbar-track {
    background-color: transparent;
  }
  @media screen and (max-width: 768px) {
    height: 100%;
    padding-right: 0;
  }
`;

const ContentContainer = styled.div`
  width: 100%;
  @media screen and (max-width: 768px) {
    width: 100%;
    padding: 0 10px 40px 16px;
    box-sizing: border-box;
  }
`;

const PlacesGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  @media screen and (max-width: 768px) {
    grid-template-columns: 1fr;
    gap: 20px;
  }
`;

const LoadMoreTrigger = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60px;
  margin-top: 20px;
`;

const LoadingContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
`;

const ErrorContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: red;
`;

const Btn = styled.div`
  display: none;

  @media screen and (max-width: 768px) {
    display: flex;
    font-size: 14px;
  }
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#c3c3c3' : '#6f6f6f')};
  border-radius: 0px;
  font-size: 16px;
  border-bottom: 0.5px solid ${({ theme }) => (theme.textColor === '#ffffff' ? '#c3c3c3' : '#6f6f6f')};
  width: fit-content;
  gap: 6px;
  margin-bottom: 18px;
  cursor: pointer;
`;
