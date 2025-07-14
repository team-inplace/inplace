import { useQueries } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { InfluencerData, SpotData, UserPlaceData } from '@/types';

export const getSearchInfluencerPath = () => `/search/influencer`;
export const getSearchPlacePath = () => `/search/place`;
export const getSearchVideoPath = () => `/search/video`;

export const getSearchInfluencers = async (value: string) => {
  const params = new URLSearchParams({
    value: value.toString(),
  });

  const response = await fetchInstance.get<InfluencerData[]>(`${getSearchInfluencerPath()}?${params}`, {
    withCredentials: true,
  });
  return response.data;
};
export const getSearchVideos = async (value: string) => {
  const params = new URLSearchParams({
    value: value.toString(),
  });

  const response = await fetchInstance.get<SpotData[]>(`${getSearchVideoPath()}?${params}`);
  return response.data;
};
export const getSearchPlaces = async (value: string) => {
  const params = new URLSearchParams({
    value: value.toString(),
  });

  const response = await fetchInstance.get<UserPlaceData[]>(`${getSearchPlacePath()}?${params}`, {
    withCredentials: true,
  });
  return response.data;
};
export const useGetSearchData = (value: string) => {
  return useQueries({
    queries: [
      {
        queryKey: ['searchInfluencers', value],
        queryFn: () => getSearchInfluencers(value),
        enabled: !!value,
        staleTime: 1000 * 60 * 5,
      },
      {
        queryKey: ['searchVideos', value],
        queryFn: () => getSearchVideos(value),
        enabled: !!value,
        staleTime: 1000 * 60 * 5,
      },
      {
        queryKey: ['searchPlaces', value],
        queryFn: () => getSearchPlaces(value),
        enabled: !!value,
        staleTime: 1000 * 60 * 5,
      },
    ],
  });
};
