import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '../instance';
import { RequestPostLike } from '@/types';

export const postPostLikePath = () => `/posts/likes`;
const postPostLike = async ({ postId, likes }: RequestPostLike) => {
  const response = await fetchInstance.post(
    postPostLikePath(),
    {
      postId,
      likes,
    },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostPostLike = () => {
  return useMutation({
    mutationFn: ({ postId, likes }: RequestPostLike) => postPostLike({ postId, likes }),
  });
};
