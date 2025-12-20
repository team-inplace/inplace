import { useSuspenseQuery } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { PageableData, ReviewData } from '@/types';

interface GetReviewParams {
  page: number;
  size: number;
  id: string;
}

export const getReviewPath = (id: string) => `/places/${id}/reviews`;
export const getReview = async ({ page, size, id }: GetReviewParams) => {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
  });

  const response = await getFetchInstance().get<PageableData<ReviewData>>(`${getReviewPath(id)}?${params}`, {
    withCredentials: true,
  });
  return response.data;
};
export const useGetReview = ({ page, size, id }: GetReviewParams) => {
  return useSuspenseQuery({ queryKey: ['review', id, page, size], queryFn: () => getReview({ page, size, id }) });
};
