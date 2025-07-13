import { useQuery } from '@tanstack/react-query';

import { fetchInstance } from '../instance';
import { FilterParams, LocationData, MarkerData } from '@/types';

export const getAllMarkersPath = () => `/places/all`;

export const getAllMarkers = async (
  location: LocationData,
  filters: FilterParams,
  center: { lat: number; lng: number },
) => {
  const { topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude } = location;
  const { categories, influencers } = filters;

  const params = new URLSearchParams({
    topLeftLongitude: topLeftLongitude.toString(),
    topLeftLatitude: topLeftLatitude.toString(),
    bottomRightLongitude: bottomRightLongitude.toString(),
    bottomRightLatitude: bottomRightLatitude.toString(),
    longitude: center.lng.toString(),
    latitude: center.lat.toString(),
    categories: categories.join(','),
    influencers: influencers.join(','),
  });

  const response = await fetchInstance.get<MarkerData[]>(`${getAllMarkersPath()}?${params}`);
  return response.data;
};

interface QueryParams {
  location: LocationData;
  filters: FilterParams;
  center: { lat: number; lng: number };
}

export const useGetAllMarkers = ({ location, filters, center }: QueryParams, enabled?: boolean) => {
  return useQuery({
    queryKey: ['allMarkers', location, filters, center],
    queryFn: () => getAllMarkers(location, filters, center),
    staleTime: 1000 * 60 * 5,
    enabled,
  });
};
