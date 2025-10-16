import { useQuery } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';
import { PageableData, InfluencerData } from '@/types';

interface GetAllInfluencersParams {
  value: string;
  page: number;
  size: number;
}

export const getSearchInfluencerPath = () => `/search/page/influencer`;
export const getSearchInfluencers = async ({ value, page, size }: GetAllInfluencersParams) => {
  const params = new URLSearchParams({
    value: value.toString(),
    page: page.toString(),
    size: size.toString(),
  });

  const response = await fetchInstance.get<PageableData<InfluencerData>>(`${getSearchInfluencerPath()}?${params}`);
  return response.data;
};

export const useGetSearchInfluencers = ({ value, page, size }: GetAllInfluencersParams) => {
  return useQuery({
    queryKey: ['searchInfluencers', page, size, value],
    queryFn: () => getSearchInfluencers({ page, size, value }),
    staleTime: 1000 * 60 * 5,
    enabled: !!value,
  });
};
