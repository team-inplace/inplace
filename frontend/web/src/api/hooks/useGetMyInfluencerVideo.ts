import { useQuery } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { SpotData } from '@/types';

export const getMyInfluencerVideoPath = () => `/videos/my`;

export const getMyInfluencerVideo = async () => {
  const response = await getFetchInstance().get<SpotData[]>(getMyInfluencerVideoPath(), { withCredentials: true });
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
