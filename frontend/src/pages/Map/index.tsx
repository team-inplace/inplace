import { useState, useMemo, useCallback } from 'react';
import styled from 'styled-components';
import DropdownMenu from '@/components/Map/DropdownMenu';
import MapWindow from '@/components/Map/MapWindow';
import PlaceSection from '@/components/Map/PlaceSection';
import Chip from '@/components/common/Chip';
import { Text } from '@/components/common/typography/Text';
import locationOptions from '@/utils/constants/LocationOptions';
import categoryOptions from '@/utils/constants/CategoryOptions';
import useGetDropdownName from '@/api/hooks/useGetDropdownName';
import useTouchDrag from '@/hooks/Map/useTouchDrag';
import useMapState from '@/hooks/Map/useMapState';

type SelectedOption = {
  main: string;
  sub?: string;
  lat?: number;
  lng?: number;
};

export default function MapPage() {
  const { data: influencerOptions } = useGetDropdownName();
  const [selectedInfluencers, setSelectedInfluencers] = useState<string[]>([]);
  const [selectedLocations, setSelectedLocations] = useState<SelectedOption[]>([]);
  const [selectedCategories, setSelectedCategories] = useState<string[]>([]);
  const {
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
  } = useMapState();
  const { translateY, setTranslateY, handleTouchStart, handleTouchMove, handleTouchEnd } =
    useTouchDrag(setIsListExpanded);

  const filters = useMemo(
    () => ({
      categories: selectedCategories,
      influencers: selectedInfluencers,
      location: selectedLocations,
    }),
    [selectedCategories, selectedInfluencers, selectedLocations],
  );

  const handleLocationChange = useCallback((value: SelectedOption) => {
    setSelectedLocations((prev) => {
      // 중복 생성 방지
      const isDuplicate = prev.some((loc) => loc.main === value.main && loc.sub === value.sub);
      if (isDuplicate) return prev;

      if (value.sub === '전체' || !value.sub) {
        const hasAll = prev.some((loc) => loc.main === value.main && loc.sub === '전체');
        if (hasAll) return prev;
        return [...prev, value];
      }

      if (value.sub) {
        return [...prev, value];
      }
      return prev;
    });

    if (value.lat && value.lng) {
      setCenter({ lat: value.lat, lng: value.lng });
    }
  }, []);

  const handleInfluencerChange = useCallback((value: { main: string }) => {
    setSelectedInfluencers((prev) => {
      // 이미 선택된 인플루언서인 경우 추가하지 않음
      if (prev.includes(value.main)) return prev;
      return [...prev, value.main];
    });
  }, []);

  const handleCategoryChange = useCallback((value: { main: string }) => {
    setSelectedCategories((prev) => {
      if (prev.includes(value.main)) return prev;
      return [...prev, value.main];
    });
  }, []);

  const handleClearLocation = useCallback((locationToRemove: SelectedOption) => {
    setSelectedLocations((prev) =>
      prev.filter((location) => !(location.main === locationToRemove.main && location.sub === locationToRemove.sub)),
    );
  }, []);

  const handleClearInfluencer = useCallback((influencerToRemove: string) => {
    setSelectedInfluencers((prev) => prev.filter((influencer) => influencer !== influencerToRemove));
  }, []);

  const handleClearCategory = useCallback((categoryToRemove: string) => {
    setSelectedCategories((prev) => prev.filter((category) => category !== categoryToRemove));
  }, []);

  const handleListExpand = useCallback(() => {
    setIsListExpanded((prev) => {
      const newExpandedState = !prev;
      setTranslateY(newExpandedState ? 0 : window.innerHeight);
      return newExpandedState;
    });
  }, []);

  return (
    <PageContainer>
      <Wrapper>
        <Text size="l" weight="bold">
          지도
        </Text>
        <DropdownContainer>
          <DropdownMenu
            options={locationOptions}
            multiLevel
            onChange={handleLocationChange}
            placeholder="위치"
            type="location"
            selectedOptions={selectedLocations}
          />
          <DropdownMenu
            options={influencerOptions}
            onChange={handleInfluencerChange}
            placeholder="인플루언서"
            type="influencer"
            defaultValue={undefined}
            selectedOptions={selectedInfluencers}
          />
          <DropdownMenu
            options={categoryOptions}
            onChange={handleCategoryChange}
            placeholder="카테고리"
            type="category"
            selectedOptions={selectedCategories}
          />
        </DropdownContainer>
        <Chip
          selectedLocations={selectedLocations}
          selectedInfluencers={selectedInfluencers}
          selectedCategories={selectedCategories}
          onClearLocation={handleClearLocation}
          onClearInfluencer={handleClearInfluencer}
          onClearCategory={handleClearCategory}
        />
      </Wrapper>
      <MapWindow
        center={center}
        onBoundsChange={handleBoundsChange}
        onCenterChange={handleCenterChange}
        filters={filters}
        placeData={placeData}
        selectedPlaceId={selectedPlaceId}
        onPlaceSelect={handlePlaceSelect}
        isListExpanded={isListExpanded}
        onListExpand={handleListExpand}
      />
      <PlaceSectionDesktop>
        <PlaceSection
          mapBounds={mapBounds}
          filters={filters}
          center={center}
          onGetPlaceData={handleGetPlaceData}
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
        <PlaceSection
          mapBounds={mapBounds}
          filters={filters}
          center={center}
          onGetPlaceData={handleGetPlaceData}
          onPlaceSelect={handlePlaceSelect}
          selectedPlaceId={selectedPlaceId}
          isListExpanded={isListExpanded}
          onListExpand={handleListExpand}
        />
      </MobilePlaceSection>
    </PageContainer>
  );
}

const PageContainer = styled.div`
  padding: 6px 0;
  @media screen and (max-width: 768px) {
    width: 100%;
    align-items: center;
    touch-action: none;
  }
`;

const Wrapper = styled.div`
  @media screen and (max-width: 768px) {
    display: flex;
    flex-direction: column;
    align-items: center;
    > * {
      width: 90%;
    }
  }
`;

const DropdownContainer = styled.div`
  display: flex;
  gap: 14px;
  padding-top: 16px;

  @media screen and (max-width: 768px) {
    width: 90%;
    gap: 12px;
    padding-top: 12px;
    z-index: 9;

    flex-wrap: wrap;
  }
`;

const PlaceSectionDesktop = styled.div`
  margin-top: 10px;
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
