import { useMutation } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { RequestPlaceReview } from '@/types';

export const postPlaceReviewPath = (uuid: string) => `/reviews/${uuid}`;
const postPlaceReview = async (uuid: string, { likes, comments }: RequestPlaceReview) => {
  const response = await getFetchInstance().post(
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
