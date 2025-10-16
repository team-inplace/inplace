import { useSuspenseQuery } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';
import { PageableData, InfluencerData } from '@/types';
import { getInfluencerPath } from './useGetMain';

interface GetAllInfluencersParams {
  page: number;
  size: number;
}

export const getAllInfluencers = async ({ page, size }: GetAllInfluencersParams) => {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
  });

  const response = await fetchInstance.get<PageableData<InfluencerData>>(`${getInfluencerPath()}?${params}`, {
    withCredentials: true,
  });
  return response.data;
};

export const useGetAllInfluencers = ({ page, size }: GetAllInfluencersParams) => {
  return useSuspenseQuery({
    queryKey: ['AllInfluencers', page, size],
    queryFn: () => getAllInfluencers({ page, size }),
  });
};
