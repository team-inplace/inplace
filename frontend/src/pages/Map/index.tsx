import { useState, useMemo, useCallback, useRef } from 'react';
import styled from 'styled-components';
import MapWindow from '@/components/Map/MapWindow';
import PlaceSection from '@/components/Map/PlaceSection';
import Chip from '@/components/common/Chip';
import useGetDropdownCategory from '@/api/hooks/useGetDropdownCategory';
import useGetDropdownName from '@/api/hooks/useGetDropdownName';
import useTouchDrag from '@/hooks/Map/useTouchDrag';
import useMapState from '@/hooks/Map/useMapState';
import DropdownFilterBar, { FilterBarItem } from '@/components/Map/DropdownFilterBar';
import MapSearchBar from '@/components/Map/MapSearchBar';
import useClickOutside from '@/hooks/useClickOutside';

export default function MapPage() {
  const { data: influencerOptions } = useGetDropdownName();
  const { data: categoryOptions = [] } = useGetDropdownCategory();
  const [selectedInfluencers, setSelectedInfluencers] = useState<string[]>([]);
  const [selectedCategories, setSelectedCategories] = useState<{ id: number; label: string }[]>([]);
  const [selectedPlaceName, setSelectedPlaceName] = useState<string>('');
  const [isFilterBarOpened, setIsFilterBarOpened] = useState(false);
  const [isInitialLoad, setIsInitialLoad] = useState(true);
  const [isChangedLocation, setIsChangedLocation] = useState<{ lat: number; lng: number } | null>(null);
  const filterRef = useRef<HTMLDivElement | null>(null);
  const {
    center,
    setCenter,
    setMapBounds,
    mapBounds,
    isListExpanded,
    selectedPlaceId,
    placeData,
    setIsListExpanded,
    handlePlaceSelect,
    handleGetPlaceData,
  } = useMapState();
  const { translateY, setTranslateY, handleTouchStart, handleTouchMove, handleTouchEnd } =
    useTouchDrag(setIsListExpanded);

  useClickOutside([filterRef], () => {
    if (isFilterBarOpened) setIsFilterBarOpened(false);
  });

  const filters = useMemo(
    () => ({
      categories: selectedCategories.map((cat) => cat.id),
      influencers: selectedInfluencers,
    }),
    [selectedCategories, selectedInfluencers],
  );

  const filtersWithPlaceName = useMemo(
    () => ({
      categories: selectedCategories.map((cat) => cat.id),
      influencers: selectedInfluencers,
      placeName: selectedPlaceName,
    }),
    [selectedCategories, selectedInfluencers, selectedPlaceName],
  );

  const handleInfluencerChange = useCallback((value: { main: string }) => {
    setSelectedInfluencers((prev) => {
      // 이미 선택된 인플루언서인 경우 추가하지 않음
      if (prev.includes(value.main)) return prev;
      return [...prev, value.main];
    });
  }, []);

  const handleCategoryChange = useCallback((value: { main: string; id?: number }) => {
    if (typeof value.id === 'number') {
      setSelectedCategories((prev) => {
        if (prev.some((category) => category.id === value.id)) return prev;
        return [...prev, { id: value.id!, label: value.main }];
      });
    }
  }, []);

  const handleInfluencerClear = useCallback((influencerToRemove: string) => {
    setSelectedInfluencers((prev) => prev.filter((influencer) => influencer !== influencerToRemove));
  }, []);

  const handleCategoryClear = useCallback((categoryToRemove: number) => {
    setSelectedCategories((prev) => prev.filter((category) => category.id !== categoryToRemove));
  }, []);

  const handleListExpand = useCallback(() => {
    setIsListExpanded((prev) => {
      const newExpandedState = !prev;
      setTranslateY(newExpandedState ? 0 : window.innerHeight);
      return newExpandedState;
    });
  }, []);

  const dropdownItems: FilterBarItem[] = [
    {
      type: 'dropdown',
      id: 'influencer',
      props: {
        options: influencerOptions,
        onChange: handleInfluencerChange,
        isMobileOpen: true,
        placeholder: '인플루언서',
        type: 'influencer',
        width: 140,
        selectedOptions: selectedInfluencers,
      },
    },
    { type: 'separator', id: 'sep2' },
    {
      type: 'dropdown',
      id: 'category',
      props: {
        options: categoryOptions,
        onChange: handleCategoryChange,
        isMobileOpen: false,
        placeholder: '카테고리',
        type: 'category',
        width: 120,
        selectedOptions: selectedCategories.map((cat) => cat.id),
      },
    },
  ];

  return (
    <PageContainer>
      <Wrapper>
        <FilterContainer>
          <MobileFilterContainer>
            <FilterButtonContainer aria-label="모바일 지도 필터" onClick={() => setIsFilterBarOpened((prev) => !prev)}>
              필터
            </FilterButtonContainer>
            <MapSearchBar setIsChangedLocation={setIsChangedLocation} setSelectedPlaceName={setSelectedPlaceName} />
          </MobileFilterContainer>
          <DesktopDropdownSection>
            <DropdownFilterBar items={dropdownItems} />
          </DesktopDropdownSection>
        </FilterContainer>

        <Chip
          selectedInfluencers={selectedInfluencers}
          selectedCategories={selectedCategories}
          onClearInfluencer={handleInfluencerClear}
          onClearCategory={handleCategoryClear}
        />
      </Wrapper>
      {isFilterBarOpened && (
        <>
          <Background onClick={() => setIsFilterBarOpened(false)} />
          <MobileDropdownSection ref={filterRef}>
            <CloseBtn onClick={() => setIsFilterBarOpened(false)}>X</CloseBtn>
            <DropdownFilterBar items={dropdownItems} />
          </MobileDropdownSection>
        </>
      )}
      <MapWindow
        center={center}
        setCenter={setCenter}
        setMapBounds={setMapBounds}
        mapBounds={mapBounds}
        isInitialLoad={isInitialLoad}
        setIsInitialLoad={setIsInitialLoad}
        filters={filters}
        filtersWithPlaceName={filtersWithPlaceName}
        placeData={placeData}
        selectedPlaceId={selectedPlaceId}
        isChangedLocation={isChangedLocation}
        onPlaceSelect={handlePlaceSelect}
        isListExpanded={isListExpanded}
        onListExpand={handleListExpand}
      />
      <PlaceSectionDesktop>
        <PlaceSection
          center={center}
          mapBounds={mapBounds}
          filters={filters}
          isInitialLoad={isInitialLoad}
          filtersWithPlaceName={filtersWithPlaceName}
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
          filtersWithPlaceName={filtersWithPlaceName}
          center={center}
          isInitialLoad={isInitialLoad}
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
  padding: 16px 0;
  @media screen and (max-width: 768px) {
    position: relative;
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

const FilterContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;

  @media screen and (max-width: 768px) {
    width: 90%;
    z-index: 100;
    gap: 10px;
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

const MobileFilterContainer = styled.div`
  @media screen and (max-width: 768px) {
    width: 100%;
    z-index: 2001;
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 4px;
  }
`;

const DesktopDropdownSection = styled.div`
  @media screen and (max-width: 768px) {
    display: none;
  }
`;

const FilterButtonContainer = styled.div`
  display: none;

  @media screen and (max-width: 768px) {
    display: block;
    flex-shrink: 1;
    z-index: 9;
    padding: 8px 6px;
    background: white;
    color: #292929;
    font-size: 12px;
    border: 1.5px solid #a5a5a5;
    border-radius: 6px;
  }
`;

const MobileDropdownSection = styled.div`
  display: none;

  @media screen and (max-width: 768px) {
    display: flex;
    flex-direction: column;
    position: fixed;
    align-items: end;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 60vh;
    background-color: ${({ theme }) => theme.backgroundColor};
    z-index: 101;
    padding: 10px 20px;
    border-top-left-radius: 16px;
    border-top-right-radius: 16px;
    animation: slideUp 0.3s ease-out forwards;
    box-sizing: border-box;

    @keyframes slidUp {
      from {
        transform: translateY(100%);
      }
      to {
        transform: translateY(0%);
      }
    }
  }
`;
const CloseBtn = styled.button`
  display: none;

  @media screen and (max-width: 768px) {
    display: block;
    top: 10px;
    right: 4px;
    font-size: 16px;
    padding: 10px 0px;
    background: none;
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : '#a5a5a5')};
    border: none;
  }
`;

const Background = styled.div`
  display: none;

  @media screen and (max-width: 768px) {
    display: block;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 100;
    animation: fadeIn 0.3s ease-out forwards;

    @keyframes fadeIn {
      from {
        opacity: 0;
      }
      to {
        opacity: 1;
      }
    }
  }
`;
