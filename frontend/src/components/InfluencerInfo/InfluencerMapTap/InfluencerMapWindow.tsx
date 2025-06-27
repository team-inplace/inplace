import { useState, useCallback, useEffect, useRef } from 'react';
import { CustomOverlayMap, Map, MapMarker, MarkerClusterer } from 'react-kakao-maps-sdk';
import styled from 'styled-components';
import { TbCurrentLocation } from 'react-icons/tb';
import { GrPowerCycle } from 'react-icons/gr';
import Button from '@/components/common/Button';
import { LocationData, MarkerData, PlaceData } from '@/types';
import InfoWindow from './InfoWindow';
import BasicImage from '@/assets/images/basic-image.webp';
import { Text } from '@/components/common/typography/Text';
import useMapActions from '@/hooks/Map/useMapAction';
import useMarkerData from '@/hooks/Map/useMarkerData';
import useIsMobile from '@/hooks/useIsMobile';

interface MapWindowProps {
  influencerImg: string;
  markers: MarkerData[];
  placeData: PlaceData[];
  center: { lat: number; lng: number };
  setCenter: React.Dispatch<React.SetStateAction<{ lat: number; lng: number }>>;
  setMapBounds: React.Dispatch<React.SetStateAction<LocationData>>;
  selectedPlaceId: number | null;
  onCompleteFetch: (value: boolean) => void;
  onPlaceSelect: (placeId: number | null) => void;
  isListExpanded?: boolean;
  onListExpand?: (value: boolean) => void;
  onNearbySearch?: (handleNearbySearch: () => void) => void;
  isRestoredFromDetail?: boolean;
  savedZoomLevel?: number;
  setSavedZoomLevel: React.Dispatch<React.SetStateAction<number | undefined>>;
}

export default function InfluencerMapWindow({
  influencerImg,
  markers,
  center,
  setCenter,
  setMapBounds,
  placeData,
  selectedPlaceId,
  onCompleteFetch,
  onPlaceSelect,
  isListExpanded,
  onListExpand,
  onNearbySearch,
  isRestoredFromDetail = false,
  savedZoomLevel,
  setSavedZoomLevel,
}: MapWindowProps) {
  const mapRef = useRef<kakao.maps.Map | null>(null);
  const [userLocation, setUserLocation] = useState<{ lat: number; lng: number } | null>(null);
  const [isMapReady, setIsMapReady] = useState(false);
  const [hasInitialPositionSet, setHasInitialPositionSet] = useState(false);
  const { moveMapToMarker, handleCenterReset } = useMapActions({ mapRef, onPlaceSelect });
  const { markerInfo, handleMarkerClick, handleMapClick } = useMarkerData({
    selectedPlaceId,
    placeData,
    onPlaceSelect,
    moveMapToMarker,
    mapRef,
  });
  const isMobile = useIsMobile();

  const originSize = isMobile ? 26 : 34;
  const userLocationSize = isMobile ? 16 : 24;
  const selectedMarker = markers.find((m) => m.placeId === selectedPlaceId);

  const updateMapBounds = useCallback(() => {
    if (!mapRef.current) return;

    const bounds = mapRef.current.getBounds();
    const currentCenter = mapRef.current.getCenter();
    const currentZoomLevel = mapRef.current.getLevel();

    const newBounds = {
      topLeftLatitude: bounds.getNorthEast().getLat(),
      topLeftLongitude: bounds.getSouthWest().getLng(),
      bottomRightLatitude: bounds.getSouthWest().getLat(),
      bottomRightLongitude: bounds.getNorthEast().getLng(),
    };

    setMapBounds(newBounds);
    setCenter({ lat: currentCenter.getLat(), lng: currentCenter.getLng() });
    setSavedZoomLevel(currentZoomLevel);
    onCompleteFetch(true);
  }, [setMapBounds, setCenter, setSavedZoomLevel, onCompleteFetch]);

  useEffect(() => {
    if (isMapReady && mapRef.current && markers.length > 0 && !hasInitialPositionSet) {
      if (isRestoredFromDetail && selectedPlaceId && selectedMarker) {
        mapRef.current.setCenter(new kakao.maps.LatLng(selectedMarker.latitude, selectedMarker.longitude));
        mapRef.current.setLevel(savedZoomLevel || 14);
      } else {
        mapRef.current.setCenter(new kakao.maps.LatLng(center.lat, center.lng));
        mapRef.current.setLevel(savedZoomLevel || 14);
      }
      setHasInitialPositionSet(true);
      updateMapBounds();
    }
  }, [
    isRestoredFromDetail,
    isMapReady,
    selectedPlaceId,
    selectedMarker,
    markers.length,
    hasInitialPositionSet,
    center,
    savedZoomLevel,
    updateMapBounds,
  ]);

  useEffect(() => {
    if (selectedPlaceId && selectedMarker && isMapReady && hasInitialPositionSet) {
      moveMapToMarker(selectedMarker.latitude, selectedMarker.longitude);
    }
  }, [selectedPlaceId, selectedMarker, moveMapToMarker, isMapReady, hasInitialPositionSet]);

  useEffect(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const userLoc = {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          };
          setUserLocation(userLoc);
        },
        (err) => {
          console.error('Geolocation error:', err);
        },
      );
    } else {
      console.warn('Geolocation is not supported by this browser.');
    }
  }, []);

  useEffect(() => {
    const timeout = setTimeout(() => {
      if (!mapRef.current && !hasInitialPositionSet) {
        alert('지도를 불러오지 못했어요. 네트워크 상태를 확인한 후 새로고침 해주세요.');
      }
    }, 6000);

    return () => clearTimeout(timeout);
  }, [hasInitialPositionSet]);

  const handleNearbySearch = useCallback(() => {
    if (!mapRef.current) return;
    const currentCenter = mapRef.current.getCenter();
    setCenter({ lat: currentCenter.getLat(), lng: currentCenter.getLng() });
    onPlaceSelect(null);
    updateMapBounds();
  }, [setCenter, onPlaceSelect, updateMapBounds]);

  useEffect(() => {
    if (onNearbySearch) {
      onNearbySearch(handleNearbySearch);
    }
  }, [handleNearbySearch, onNearbySearch]);
  const handleMapChange = useCallback(() => {
    updateMapBounds();
  }, [updateMapBounds]);

  return (
    <>
      <MapContainer>
        <Map
          center={center}
          style={{ width: '100%', height: 'auto', aspectRatio: isMobile ? '1' : '1.65/1' }}
          level={savedZoomLevel || 14}
          onCreate={(map) => {
            mapRef.current = map;
            setIsMapReady(true);
          }}
          onZoomChanged={() => {
            if (mapRef.current) {
              setSavedZoomLevel(mapRef.current.getLevel());
              handleMapChange();
            }
          }}
          onDragEnd={handleMapChange}
          onClick={handleMapClick}
        >
          {userLocation && (
            <MapMarker
              position={userLocation}
              image={{
                src: 'https://i.ibb.co/4gGFjRx/circle.webp',
                size: { width: userLocationSize, height: userLocationSize },
              }}
            />
          )}
          <MarkerClusterer averageCenter minLevel={10} minClusterSize={2}>
            {markers.map((place) => (
              <MapMarker
                key={place.placeId}
                onClick={(marker) => {
                  handleMarkerClick(place.placeId, marker);
                }}
                zIndex={selectedPlaceId === place.placeId ? 999 : 1}
                position={{
                  lat: place.latitude,
                  lng: place.longitude,
                }}
                image={{
                  src: influencerImg || BasicImage,
                  size: {
                    width: selectedPlaceId === place.placeId ? originSize + 14 : originSize,
                    height: selectedPlaceId === place.placeId ? originSize + 14 : originSize,
                  },
                }}
              />
            ))}
          </MarkerClusterer>
          {selectedPlaceId !== null && selectedMarker && markerInfo && hasInitialPositionSet && (
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
            aria-label="인플루언서 내위치"
            onClick={() => userLocation && handleCenterReset(userLocation)}
            variant="white"
            size="small"
          >
            <TbCurrentLocation size={20} />
          </StyledBtn>
        </ResetButtonContainer>
        {!isListExpanded && (
          <ListViewButton aria-label="인플루언서 지도 목록보기" onClick={() => onListExpand && onListExpand(true)}>
            <Text size="xs" variant="white" weight="normal">
              목록 보기
            </Text>
          </ListViewButton>
        )}
      </MapContainer>
      <Btn aria-label="인플루언서 지도 리프레시" onClick={handleNearbySearch}>
        <GrPowerCycle />
        현재 위치에서 장소정보 보기
      </Btn>
    </>
  );
}

const MapContainer = styled.div`
  position: relative;
  width: 100%;
  padding-bottom: 20px;
  color: black;
`;

const ResetButtonContainer = styled.div`
  position: absolute;
  bottom: 46px;
  right: 30px;
  z-index: 10;

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
const Btn = styled.div`
  display: flex;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#c3c3c3' : '#6f6f6f')};
  border-radius: 0px;
  font-size: 16px;
  border-bottom: 0.5px solid ${({ theme }) => (theme.textColor === '#ffffff' ? '#c3c3c3' : '#6f6f6f')};
  width: fit-content;
  padding-bottom: 4px;
  margin-bottom: 10px;
  gap: 6px;
  cursor: pointer;

  @media screen and (max-width: 768px) {
    display: none;
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
    z-index: 10;
    align-items: center;
    gap: 4px;
  }
`;
