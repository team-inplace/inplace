import { useSuspenseInfiniteQuery } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';
import { PageableData, UserPlaceData } from '@/types';

export const getUserPlacePath = () => `/users/places`;
export const getUserPlace = async (page: number, size: number) => {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
  });
  const response = await fetchInstance.get<PageableData<UserPlaceData>>(`${getUserPlacePath()}?${params}`, {
    withCredentials: true,
  });
  return response.data;
};
export const useGetUserPlace = (size: number) => {
  return useSuspenseInfiniteQuery({
    queryKey: ['UserPlace', size],
    queryFn: ({ pageParam = 0 }) => getUserPlace(pageParam, size),
    initialPageParam: 0,
    getNextPageParam: (lastPage) => {
      return lastPage.last ? undefined : lastPage.number + 1;
    },
    staleTime: 1000 * 60 * 5,
  });
};
