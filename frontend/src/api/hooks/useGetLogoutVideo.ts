import { useQueries } from '@tanstack/react-query';

import { fetchInstance } from '../instance';
import { SpotData } from '@/types';

export const getCoolEatsVideoPath = () => `/videos/cool/eats`;
export const getCoolPlaysVideoPath = () => `/videos/cool/plays`;
export const getNewVideoPath = () => `/videos/new`;

export const getCoolEatsVideo = async () => {
  const response = await fetchInstance.get<SpotData[]>(getCoolEatsVideoPath());
  return response.data;
};
export const getCoolPlaysVideo = async () => {
  const response = await fetchInstance.get<SpotData[]>(getCoolPlaysVideoPath());
  return response.data;
};
export const getNewVideo = async () => {
  const response = await fetchInstance.get<SpotData[]>(getNewVideoPath());
  return response.data;
};
export const useGetLogoutVideo = (enabled: boolean) => {
  return useQueries({
    queries: [
      { queryKey: ['coolEatsVideo'], queryFn: getCoolEatsVideo, staleTime: 1000 * 60 * 5, enabled },
      { queryKey: ['coolPlaysVideo'], queryFn: getCoolPlaysVideo, staleTime: 1000 * 60 * 5, enabled },
      { queryKey: ['newVideo'], queryFn: getNewVideo, staleTime: 1000 * 60 * 5, enabled },
    ],
  });
};
