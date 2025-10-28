import { useMutation } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { RequestPlaceLike } from '@/types';

export const postPlaceLikePath = () => `/places/likes`;
const postPlaceLike = async ({ placeId, likes }: RequestPlaceLike) => {
  const response = await getFetchInstance().post(
    postPlaceLikePath(),
    {
      placeId,
      likes,
    },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostPlaceLike = () => {
  return useMutation({
    mutationFn: ({ placeId, likes }: RequestPlaceLike) => postPlaceLike({ placeId, likes }),
  });
};
