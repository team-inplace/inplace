import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '../instance';
import { RequestPlaceLike } from '@/types';

export const postPlaceLikePath = () => `/places/likes`;
const postPlaceLike = async ({ placeId, likes }: RequestPlaceLike) => {
  const response = await fetchInstance.post(
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
