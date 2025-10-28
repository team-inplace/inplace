import { useQuery } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { FilterParams, MarkerData } from '@/types';

export const getSearchPlaceMarkersPath = () => `/places/all/search`;

export const getSearchPlaceMarkers = async (filters: FilterParams) => {
  const { categories, influencers, placeName = '' } = filters;

  const params = new URLSearchParams({
    categories: categories.join(','),
    influencers: influencers.join(','),
    placeName: placeName.toString(),
  });

  const response = await getFetchInstance().get<MarkerData[]>(`${getSearchPlaceMarkersPath()}?${params}`);
  return response.data;
};

export const useGetSearchPlaceMarkers = ({ filters }: { filters: FilterParams }, enabled?: boolean) => {
  return useQuery({
    queryKey: ['searchPlaceMarkers', filters],
    queryFn: () => getSearchPlaceMarkers(filters),
    staleTime: 1000 * 60 * 5,
    enabled,
  });
};
