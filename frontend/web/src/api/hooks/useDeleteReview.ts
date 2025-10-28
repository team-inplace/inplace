import { useMutation } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const deleteReviewPath = (id: string) => `/reviews/${id}`;
const deleteReview = async (id: string) => {
  const response = await getFetchInstance().delete(deleteReviewPath(id), { withCredentials: true });
  return response.data;
};

export const useDeleteReview = () => {
  return useMutation({
    mutationFn: (id: string) => deleteReview(id),
  });
};
