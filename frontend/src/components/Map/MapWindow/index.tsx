import { useState, useCallback, useEffect, useRef } from 'react';
import { CustomOverlayMap, Map, MapMarker, MarkerClusterer } from 'react-kakao-maps-sdk';
import styled, { keyframes } from 'styled-components';
import { TbCurrentLocation } from 'react-icons/tb';
import { GrPowerCycle } from 'react-icons/gr';
import { IoMdInformationCircleOutline } from 'react-icons/io';
import Button from '@/components/common/Button';
import { FilterParams, LocationData, PlaceData } from '@/types';
import { useGetAllMarkers } from '@/api/hooks/useGetAllMarkers';
import InfoWindow from '@/components/InfluencerInfo/InfluencerMapTap/InfoWindow';
import PlaysMarker from '@/assets/images/PlaysMarker.webp';
import EatsMarker from '@/assets/images/EatsMarker.webp';
import { Text } from '@/components/common/typography/Text';
import nowLocation from '@/assets/images/now_location.webp';
import Loading from '@/components/common/layouts/Loading';
import useMapActions from '@/hooks/Map/useMapAction';
import useMarkerData from '@/hooks/Map/useMarkerData';
import useIsMobile from '@/hooks/useIsMobile';
import { useGetSearchPlaceMarkers } from '@/api/hooks/useGetSearchPlaceMarker';

interface MapWindowProps {
  center: { lat: number; lng: number };
  setCenter: React.Dispatch<React.SetStateAction<{ lat: number; lng: number }>>;
  setMapBounds: React.Dispatch<React.SetStateAction<LocationData>>;
  mapBounds: LocationData;
  isInitialLoad: boolean;
  setIsInitialLoad: React.Dispatch<React.SetStateAction<boolean>>;
  filters: {
    categories: number[];
    influencers: string[];
  };
  filtersWithPlaceName: FilterParams;
  placeData: PlaceData[];
  isChangedLocation: { lat: number; lng: number } | null;
  selectedPlaceId: number | null;
  onPlaceSelect: (placeId: number | null) => void;
  isListExpanded?: boolean;
  onListExpand?: () => void;
}

export default function MapWindow({
  center,
  setCenter,
  mapBounds,
  setMapBounds,
  isInitialLoad,
  setIsInitialLoad,
  filters,
  filtersWithPlaceName,
  placeData,
  isChangedLocation,
  selectedPlaceId,
  onPlaceSelect,
  isListExpanded,
  onListExpand,
}: MapWindowProps) {
  const GEOLOCATION_CONFIG = {
    maximumAge: 500000,
    timeout: 5000,
  };
  const DEFAULT_MAP_ZOOM_LEVEL = 4;

  const mapRef = useRef<kakao.maps.Map | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isMapReady, setIsMapReady] = useState(false);
  const [userLocation, setUserLocation] = useState<{ lat: number; lng: number } | null>(null);
  const [showSearchButton, setShowSearchButton] = useState(false);
  const [showNoMarkerMessage, setShowNoMarkerMessage] = useState(false);
  const isMobile = useIsMobile();
  const { moveMapToMarker, handleCenterReset } = useMapActions({ mapRef, onPlaceSelect });
  const { markerInfo, handleMarkerClick, handleMapClick } = useMarkerData({
    selectedPlaceId,
    placeData,
    onPlaceSelect,
    moveMapToMarker,
    mapRef,
  });

  const originSize = isMobile ? 26 : 34;
  const userLocationSize = isMobile ? 16 : 24;

  const { data: markers = [], isLoading: isLoadingAllMarkers } = useGetAllMarkers(
    {
      location: mapBounds,
      filters,
      center,
    },
    !isInitialLoad && !filtersWithPlaceName.placeName,
  );

  const { data: placeNameMarkers = [], isLoading: isLoadingPlaceMarkers } = useGetSearchPlaceMarkers(
    {
      filters: filtersWithPlaceName,
    },
    !!filtersWithPlaceName.placeName,
  );

  const markerListToRender = filtersWithPlaceName.placeName ? placeNameMarkers : markers;
  const isLoadingMarker = filtersWithPlaceName.placeName ? isLoadingPlaceMarkers : isLoadingAllMarkers;

  const selectedMarker = markerListToRender.find((m) => m.placeId === selectedPlaceId) || null;

  // placeName로 검색 시 지도 범위 확장
  useEffect(() => {
    if (!mapRef.current) return;
    if (!filtersWithPlaceName.placeName) return;
    if (!placeNameMarkers || placeNameMarkers.length === 0) return;

    const bounds = new kakao.maps.LatLngBounds();
    placeNameMarkers.forEach((marker) => {
      bounds.extend(new kakao.maps.LatLng(marker.latitude, marker.longitude));
    });

    mapRef.current.setBounds(bounds);
    setShowSearchButton(false);
  }, [placeNameMarkers, filtersWithPlaceName.placeName]);

  // 초기 접속 시
  useEffect(() => {
    if (!isInitialLoad || !isMapReady) return;

    const getUserLocation = async () => {
      try {
        const position: GeolocationPosition = await new Promise((resolve, reject) => {
          navigator.geolocation.getCurrentPosition(resolve, reject, GEOLOCATION_CONFIG);
        });

        const userLoc = {
          lat: position.coords.latitude,
          lng: position.coords.longitude,
        };

        setUserLocation(userLoc);

        if (mapRef.current) {
          mapRef.current.setCenter(new kakao.maps.LatLng(userLoc.lat, userLoc.lng));
          setCenter(userLoc);
          updateMapBounds();
        }

        setIsLoading(false);
        setIsInitialLoad(false);
      } catch (error) {
        console.error('Geolocation error:', error);

        setIsLoading(false);
        updateMapBounds();
        setIsInitialLoad(false);

        if (error instanceof GeolocationPositionError) {
          if (error.code === error.PERMISSION_DENIED) {
            alert('위치 권한이 차단되었습니다. 위치를 수동으로 설정하세요.');
          } else if (error.code === error.POSITION_UNAVAILABLE) {
            alert('위치 정보를 가져올 수 없습니다.');
          } else if (error.code === error.TIMEOUT) {
            alert('위치 정보 요청이 시간 초과되었습니다. 다시 시도해주세요.');
          }
        }
      }
    };

    getUserLocation();
  }, [isInitialLoad, isMapReady]);

  useEffect(() => {
    const timeout = setTimeout(() => {
      if (!mapRef.current) {
        alert('지도를 불러오지 못했어요. 네트워크 상태를 확인한 후 새로고침 해주세요.');
        setIsLoading(false);
      }
    }, 6000);

    return () => clearTimeout(timeout);
  }, []);

  const updateMapBounds = useCallback(() => {
    if (!mapRef.current) return;
    const bounds = mapRef.current.getBounds();
    const newBounds = {
      topLeftLatitude: bounds.getNorthEast().getLat(),
      topLeftLongitude: bounds.getSouthWest().getLng(),
      bottomRightLatitude: bounds.getSouthWest().getLat(),
      bottomRightLongitude: bounds.getNorthEast().getLng(),
    };
    setMapBounds(newBounds);
  }, [setMapBounds]);

  const handleNearbyClick = () => {
    if (!mapRef.current) return;
    const currentCenter = mapRef.current.getCenter();
    setCenter({ lat: currentCenter.getLat(), lng: currentCenter.getLng() });
    onPlaceSelect(null);
    // 기존 if문 = changedLocation(위치 검색하면) 이미 있는 바운더리 쓰겠다
    updateMapBounds();
    setShowSearchButton(false);
  };

  useEffect(() => {
    if (!mapRef.current || !isChangedLocation || isInitialLoad) return;
    const LocPosition = new kakao.maps.LatLng(isChangedLocation.lat, isChangedLocation.lng);
    mapRef.current.setCenter(LocPosition);
    mapRef.current.setLevel(DEFAULT_MAP_ZOOM_LEVEL);
    setCenter(isChangedLocation);
    onPlaceSelect(null);
    updateMapBounds();
    setShowSearchButton(false);
  }, [isChangedLocation?.lat, isChangedLocation?.lng]);

  // 초기 선택 시에만 이동하도록
  useEffect(() => {
    if (!selectedPlaceId || !mapRef.current) return;

    const list = filtersWithPlaceName.placeName ? placeNameMarkers : markers;
    const marker = list.find((m) => m.placeId === selectedPlaceId);
    if (marker) {
      moveMapToMarker(marker.latitude, marker.longitude);
    }
  }, [selectedPlaceId, placeNameMarkers, markers]);

  useEffect(() => {
    const isEmpty = !isLoading && !isLoadingMarker && markerListToRender.length === 0;
    if (isEmpty) {
      setShowNoMarkerMessage(true);
      const timer = setTimeout(() => {
        setShowNoMarkerMessage(false);
      }, 3000);

      return () => clearTimeout(timer);
    }
    return undefined;
  }, [markerListToRender, isLoadingMarker, isLoading]);

  return (
    <MapContainer>
      {isLoading && (
        <LoadingWrapper>
          <Loading />
          <Text size="s" weight="normal" variant="white">
            내 위치 찾는 중...
          </Text>
        </LoadingWrapper>
      )}
      {showNoMarkerMessage && (
        <NoItemMarker>
          <IoMdInformationCircleOutline size={18} />
          일치하는 장소가 없어요!
        </NoItemMarker>
      )}
      {showSearchButton && (
        <ButtonContainer>
          <Button
            aria-label="지도 리프레시"
            onClick={handleNearbyClick}
            variant="white"
            size="small"
            style={{
              fontSize: isMobile ? '12px' : '14px',
              borderRadius: '20px',
              padding: isMobile ? '16px' : '18px',
              backgroundColor: 'rgba(0, 0, 0, 0.7)',
              color: 'white',
              gap: '8px',
            }}
          >
            <GrPowerCycle />이 위치에서 장소 보기
          </Button>
        </ButtonContainer>
      )}
      <Map
        center={center}
        style={{ width: '100%', height: isMobile ? 'auto' : '570px', aspectRatio: isMobile ? '1' : 'auto' }}
        level={DEFAULT_MAP_ZOOM_LEVEL}
        onCreate={(mapInstance) => {
          mapRef.current = mapInstance;
          setIsMapReady(true);
        }}
        onCenterChanged={() => setShowSearchButton(true)}
        onZoomChanged={() => setShowSearchButton(true)}
        onClick={handleMapClick}
      >
        {userLocation && (
          <MapMarker
            position={userLocation}
            image={{
              src: nowLocation,
              size: { width: userLocationSize, height: userLocationSize },
            }}
          />
        )}
        <MarkerClusterer averageCenter minLevel={10} minClusterSize={2}>
          {markerListToRender.map((place) => (
            <MapMarker
              key={place.placeId}
              zIndex={selectedPlaceId === place.placeId ? 999 : 1}
              onClick={(marker) => {
                handleMarkerClick(place.placeId, marker);
              }}
              position={{
                lat: place.latitude,
                lng: place.longitude,
              }}
              image={{
                src: place.type === 'Eats' ? EatsMarker : PlaysMarker,
                size: {
                  width: selectedPlaceId === place.placeId ? originSize + 14 : originSize,
                  height: selectedPlaceId === place.placeId ? originSize + 14 : originSize,
                },
              }}
            />
          ))}
        </MarkerClusterer>
        {selectedPlaceId !== null && selectedMarker && markerInfo && (
          <CustomOverlayMap
            zIndex={100}
            position={{
              lat: selectedMarker.latitude,
              lng: selectedMarker.longitude,
            }}
            clickable
          >
            <InfoWindow data={markerInfo} />
          </CustomOverlayMap>
        )}
      </Map>
      <ResetButtonContainer>
        <StyledBtn
          aria-label="지도 내위치"
          onClick={() => userLocation && handleCenterReset(userLocation)}
          variant="white"
          size="small"
        >
          <TbCurrentLocation size={20} />
        </StyledBtn>
      </ResetButtonContainer>
      {!isListExpanded && (
        <ListViewButton aria-label="지도 목록보기" onClick={onListExpand}>
          <Text size="xs" weight="normal" variant="white">
            목록 보기
          </Text>
        </ListViewButton>
      )}
    </MapContainer>
  );
}

const MapContainer = styled.div`
  position: relative;
  width: 100%;
  padding-bottom: 20px;
  color: black;
`;
const LoadingWrapper = styled.div`
  position: absolute;
  z-index: 99;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  background-color: #29292963;

  > div {
    padding: 4px 0px;
    height: auto;
    transform: translateY(-50%);
  }

  .loader {
    border-left-color: #e6e6e6;
    padding: 0px;
  }
`;

const ButtonContainer = styled.div`
  position: absolute;
  top: 5%;
  left: 50%;
  transform: translateX(-50%);
  z-index: 5;
`;

const ResetButtonContainer = styled.div`
  position: absolute;
  bottom: 46px;
  right: 30px;
  z-index: 9;

  @media screen and (max-width: 768px) {
    bottom: 34px;
    right: 14px;
  }
`;

const StyledBtn = styled(Button)`
  width: 40px;
  height: 40px;
  box-shadow: 1px 1px 2px #707070;

  @media screen and (max-width: 768px) {
    width: 28px;
    height: 28px;
    box-shadow: 1px 1px 1px #707070;
    svg {
      width: 16px;
      height: 16px;
    }
  }
`;

const ListViewButton = styled.button`
  display: none;

  @media screen and (max-width: 768px) {
    display: flex;
    position: absolute;
    bottom: 10%;
    left: 50%;
    transform: translateX(-50%);
    background: rgba(0, 0, 0, 0.7);
    border: none;
    border-radius: 20px;
    padding: 8px 16px;
    cursor: pointer;
    z-index: 9;
    align-items: center;
    gap: 4px;
  }
`;

const shakeDesktop = keyframes`
0% {
  transform: translateX(119.5%);
}
25% {
  transform: translateX(120.5%);
}
50% {
  transform: translateX(119.5%);
}
75% {
  transform: translateX(120.5%);
}
100% {
  transform: translateX(120%);
}
`;

const shakeMobile = keyframes`
  0% {
    transform: translateX(21.5%);
  }
  25% {
    transform: translateX(22.5%);
  }
  50% {
    transform: translateX(21.5%);
  }
  75% {
    transform: translateX(22.5%);
  }
  100% {
    transform: translateX(21%);
  }
`;
const fadeOut = keyframes`
0% {
  opacity: 1;
  visibility: visible;
}
80% {
  opacity: 1;
  visibility: visible;
}
100% {
  opacity: 0;
  visibility: hidden;
}
`;

const NoItemMarker = styled.div`
  position: absolute;
  top: 13%;
  width: 30%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 14px 0px;
  font-size: 14px;
  gap: 4px;
  text-align: center;
  border-radius: 6px;
  transform: translateX(120%);
  color: white;
  background-color: #292929f0;
  z-index: 100;

  svg {
    margin-bottom: 1px;
  }
  animation:
    ${shakeDesktop} 0.5s ease-out,
    ${fadeOut} 3.5s ease-out forwards;

  @media screen and (max-width: 768px) {
    width: 70%;
    padding: 8px 0px;
    transform: translateX(22%);
    font-size: 12px;
    animation:
      ${shakeMobile} 0.5s ease-out,
      ${fadeOut} 3.5s ease-out forwards;
  }
`;
