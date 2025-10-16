import { useQuery } from '@tanstack/react-query';

import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';
import { SpotData } from '@/types';

export const getMyInfluencerVideoPath = () => `/videos/my`;

export const getMyInfluencerVideo = async () => {
  const response = await fetchInstance.get<SpotData[]>(getMyInfluencerVideoPath(), { withCredentials: true });
  return response.data;
};
export const useGetMyInfluencerVideo = (enabled: boolean) => {
  return useQuery({
    queryKey: ['myInfluencerVideo'],
    queryFn: getMyInfluencerVideo,
    staleTime: 1000 * 60 * 5,
    enabled,
  });
};
