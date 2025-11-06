import { useSuspenseInfiniteQuery } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { InfluencerData, PageableData } from '@/types';

export const getUserInfluencerPath = () => `/users/influencers`;
export const getUserInfluencer = async (page: number, size: number) => {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
  });

  const response = await getFetchInstance().get<PageableData<InfluencerData>>(`${getUserInfluencerPath()}?${params}`, {
    withCredentials: true,
  });
  return response.data;
};
export const useGetUserInfluencer = (size: number) => {
  return useSuspenseInfiniteQuery({
    queryKey: ['UserInfluencer', size],
    queryFn: ({ pageParam = 0 }) => getUserInfluencer(pageParam, size),
    initialPageParam: 0,
    getNextPageParam: (lastPage) => {
      return lastPage.last ? undefined : lastPage.number + 1;
    },
    staleTime: 1000 * 60 * 5,
  });
};
