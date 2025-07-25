import { useSuspenseQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { InfluencerInfoData } from '@/types';

export const getInfluencerInfoPath = (id: string) => `/influencers/${id}`;
export const getInfluencerInfo = async (id: string) => {
  const response = await fetchInstance.get<InfluencerInfoData>(getInfluencerInfoPath(id), {
    withCredentials: true,
  });
  return response.data;
};
export const useGetInfluencerInfo = (id: string) => {
  return useSuspenseQuery({
    queryKey: ['influencerInfo', id],
    queryFn: () => getInfluencerInfo(id),
    staleTime: 1000 * 60 * 5,
  });
};
