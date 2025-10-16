import { useSuspenseInfiniteQuery } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';
import { PageableData, UserReviewData } from '@/types';

export const getUserReviewPath = () => `/users/reviews`;
export const getUserReview = async (page: number, size: number) => {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
  });

  const response = await fetchInstance.get<PageableData<UserReviewData>>(`${getUserReviewPath()}?${params}`, {
    withCredentials: true,
  });
  return response.data;
};
export const useGetUserReview = (size: number) => {
  return useSuspenseInfiniteQuery({
    queryKey: ['UserReview', size],
    queryFn: ({ pageParam = 0 }) => getUserReview(pageParam, size),
    initialPageParam: 0,
    getNextPageParam: (lastPage) => {
      return lastPage.last ? undefined : lastPage.number + 1;
    },
    staleTime: 1000 * 60 * 5,
  });
};
