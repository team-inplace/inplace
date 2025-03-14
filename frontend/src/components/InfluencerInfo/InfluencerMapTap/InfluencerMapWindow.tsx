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

interface MapWindowProps {
  influencerImg: string;
  markers: MarkerData[];
  placeData: PlaceData[];
  selectedPlaceId: number | null;
  onBoundsChange: (bounds: LocationData) => void;
  onCenterChange: (center: { lat: number; lng: number }) => void;
  shouldFetchPlaces: boolean;
  onCompleteFetch: (value: boolean) => void;
  onPlaceSelect: (placeId: number | null) => void;
  isListExpanded?: boolean;
  onListExpand?: (value: boolean) => void;
  onSearchNearby?: (handleSearcNearby: () => void) => void;
}

export default function InfluencerMapWindow({
  influencerImg,
  markers,
  placeData,
  selectedPlaceId,
  onBoundsChange,
  onCenterChange,
  shouldFetchPlaces,
  onCompleteFetch,
  onPlaceSelect,
  isListExpanded,
  onListExpand,
  onSearchNearby,
}: MapWindowProps) {
  const mapRef = useRef<kakao.maps.Map | null>(null);
  const [userLocation, setUserLocation] = useState<{ lat: number; lng: number } | null>(null);
  const [isMobile, setIsMobile] = useState(window.innerWidth <= 768);
  const { moveMapToMarker, handleResetCenter } = useMapActions(mapRef);
  const { markerInfo, handleMarkerClick, handleMapClick } = useMarkerData({
    selectedPlaceId,
    placeData,
    onPlaceSelect,
    moveMapToMarker,
    mapRef,
  });

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth <= 768);
    };

    window.addEventListener('resize', handleResize, { passive: true });
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  const originSize = isMobile ? 26 : 34;
  const userLocationSize = isMobile ? 16 : 24;
  const selectedMarker = markers.find((m) => m.placeId === selectedPlaceId);

  useEffect(() => {
    if (selectedPlaceId && selectedMarker) {
      moveMapToMarker(selectedMarker.latitude, selectedMarker.longitude);
    }
  }, [selectedPlaceId, selectedMarker, moveMapToMarker]);

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

  const fetchLocation = useCallback(() => {
    if (!mapRef.current) return;

    const bounds = mapRef.current.getBounds();
    const currentCenter = mapRef.current.getCenter();

    const newBounds: LocationData = {
      topLeftLatitude: bounds.getNorthEast().getLat(),
      topLeftLongitude: bounds.getSouthWest().getLng(),
      bottomRightLatitude: bounds.getSouthWest().getLat(),
      bottomRightLongitude: bounds.getNorthEast().getLng(),
    };
    onCenterChange({ lat: currentCenter.getLat(), lng: currentCenter.getLng() });
    onBoundsChange(newBounds);

    onCompleteFetch(true);
  }, [onBoundsChange, onCenterChange, onCompleteFetch]);

  // 장소 정보 업데이트가 필요한 경우 바운더리와 센터를 구해서 전달
  useEffect(() => {
    if (shouldFetchPlaces) {
      fetchLocation();
      onCompleteFetch(false);
    }
  }, [shouldFetchPlaces, fetchLocation, onCompleteFetch]);

  const handleSearchNearby = useCallback(() => {
    fetchLocation();
  }, [fetchLocation]);

  useEffect(() => {
    onSearchNearby?.(handleSearchNearby);
  }, [handleSearchNearby, onSearchNearby]);

  return (
    <>
      <MapContainer>
        <Map
          center={{
            lat: 36.2683,
            lng: 127.6358,
          }}
          style={{ width: '100%', height: 'auto', aspectRatio: isMobile ? '1' : '1.65/1' }}
          level={14}
          onCreate={(map) => {
            mapRef.current = map;
          }}
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
            aria-label="reset_btn"
            onClick={() => userLocation && handleResetCenter(userLocation)}
            variant="white"
            size="small"
          >
            <TbCurrentLocation size={20} />
          </StyledBtn>
        </ResetButtonContainer>
        {!isListExpanded && (
          <ListViewButton onClick={() => onListExpand && onListExpand(true)}>
            <Text size="xs" variant="white" weight="normal">
              목록 보기
            </Text>
          </ListViewButton>
        )}
      </MapContainer>
      <Btn onClick={handleSearchNearby}>
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
