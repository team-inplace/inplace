import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '../instance';
import { RequestPlaceReview } from '@/types';

export const postPlaceReviewPath = (uuid: string) => `/reviews/${uuid}`;
const postPlaceReview = async (uuid: string, { likes, comments }: RequestPlaceReview) => {
  const response = await fetchInstance.post(
    postPlaceReviewPath(uuid),
    {
      likes,
      comments,
    },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostPlaceReview = (uuid: string) => {
  return useMutation({
    mutationFn: ({ likes, comments }: RequestPlaceReview) => postPlaceReview(uuid, { likes, comments }),
  });
};
