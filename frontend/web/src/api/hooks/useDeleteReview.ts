import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';

export const deleteReviewPath = (id: string) => `/reviews/${id}`;
const deleteReview = async (id: string) => {
  const response = await fetchInstance.delete(deleteReviewPath(id), { withCredentials: true });
  return response.data;
};

export const useDeleteReview = () => {
  return useMutation({
    mutationFn: (id: string) => deleteReview(id),
  });
};
