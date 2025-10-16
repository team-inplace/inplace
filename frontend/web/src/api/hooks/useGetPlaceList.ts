import { useQuery } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';
import { LocationData, FilterParams, PlaceData, PageableData } from '@/types';

export const getPlaceList = async (
  location: LocationData,
  filters: FilterParams,
  center: { lat: number; lng: number },
  page: number,
  size: number,
): Promise<PageableData<PlaceData>> => {
  const { topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude } = location;
  const { categories, influencers } = filters;

  const params = new URLSearchParams({
    topLeftLongitude: topLeftLongitude.toString(),
    topLeftLatitude: topLeftLatitude.toString(),
    bottomRightLongitude: bottomRightLongitude.toString(),
    bottomRightLatitude: bottomRightLatitude.toString(),
    longitude: center.lng.toString(),
    latitude: center.lat.toString(),
    page: page.toString(),
    size: size.toString(),
    categories: categories.join(','),
    influencers: influencers.join(','),
  });

  const response = await fetchInstance.get<PageableData<PlaceData>>(`/places?${params}`, { withCredentials: true });
  return response.data;
};

export const useGetPlaceList = (
  location: LocationData,
  filters: FilterParams,
  center: { lat: number; lng: number },
  page: number,
  size: number,
  enabled: boolean,
) => {
  return useQuery<PageableData<PlaceData>, Error>({
    queryKey: ['placeList', location, filters, center, page, size],
    queryFn: () => getPlaceList(location, filters, center, page, size),
    staleTime: 1000 * 60 * 5,
    enabled,
  });
};
