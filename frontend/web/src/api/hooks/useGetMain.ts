import { useSuspenseQueries } from '@tanstack/react-query';

import { fetchInstance } from '../instance';
import { BannerData, InfluencerData, PageableData } from '@/types';

export const getBannerPath = () => `/banners`;
export const getInfluencerPath = () => `/influencers`;

export const getBanner = async () => {
  const response = await fetchInstance.get<BannerData[]>(getBannerPath());
  return response.data;
};
export const getInfluencer = async () => {
  const response = await fetchInstance.get<PageableData<InfluencerData>>(getInfluencerPath(), {
    withCredentials: true,
  });

  const limitedContent = response.data.content.slice(0, 10);
  return {
    ...response.data,
    content: limitedContent,
  };
};
export const useGetMain = () => {
  return useSuspenseQueries({
    queries: [
      {
        queryKey: ['banners'],
        queryFn: getBanner,
        staleTime: 1000 * 60 * 5,
      },
      {
        queryKey: ['influencers'],
        queryFn: getInfluencer,
        staleTime: 1000 * 60 * 5,
      },
    ],
  });
};
