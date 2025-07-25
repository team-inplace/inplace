import { useSuspenseQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';

export const getReviewInfoPath = (uuid: string) => `/reviews/${uuid}`;

export const getReviewInfo = async (uuid: string) => {
  const response = await fetchInstance.get(getReviewInfoPath(uuid), { withCredentials: true });
  return response.data;
};

export const useGetReviewInfo = (uuid: string) => {
  return useSuspenseQuery({
    queryKey: ['reviewInfo', uuid],
    queryFn: () => getReviewInfo(uuid),
    staleTime: 1000 * 60 * 5,
  });
};
