import { useQuery } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { SpotData } from '@/types';

export const getAroundVideoPath = () => `/videos`;

export const getAroundVideo = async (lat: number, lng: number) => {
  const params = new URLSearchParams({
    longitude: lng.toString(),
    latitude: lat.toString(),
  });
  const response = await getFetchInstance().get<SpotData[]>(`${getAroundVideoPath()}?${params.toString()}`, {
    withCredentials: true,
  });
  return response.data;
};

export const useGetAroundVideo = (lat: number, lng: number, enabled: boolean) => {
  return useQuery({
    queryKey: ['aroundVideo', lat, lng],
    queryFn: () => getAroundVideo(lat, lng),
    staleTime: 1000 * 60 * 5,
    enabled,
  });
};
