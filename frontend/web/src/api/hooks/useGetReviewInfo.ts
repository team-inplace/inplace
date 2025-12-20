import { useSuspenseQuery } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const getReviewInfoPath = (uuid: string) => `/reviews/${uuid}`;

export const getReviewInfo = async (uuid: string) => {
  const response = await getFetchInstance().get(getReviewInfoPath(uuid), { withCredentials: true });
  return response.data;
};

export const useGetReviewInfo = (uuid: string) => {
  return useSuspenseQuery({
    queryKey: ['reviewInfo', uuid],
    queryFn: () => getReviewInfo(uuid),
    staleTime: 1000 * 60 * 5,
  });
};
