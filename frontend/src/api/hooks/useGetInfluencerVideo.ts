import { useQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { PageableData, SpotData } from '@/types';

export const getInfluencerVideoPath = (id: string) => `/influencers/${id}/videos`;
export const getInfluencerVideo = async (id: string, page: number, size: number, sort: string) => {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
    sort,
  });
  const response = await fetchInstance.get<PageableData<SpotData>>(`${getInfluencerVideoPath(id)}?${params}`);
  return response.data;
};
export const useGetInfluencerVideo = (id: string, page: number, size: number, sort: string) => {
  return useQuery({
    queryKey: ['influencerVideo', id, page, size, sort],
    queryFn: () => getInfluencerVideo(id, page, size, sort),
  });
};
