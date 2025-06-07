import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '../instance';
import { RequestCommentLike } from '@/types';

export const postCommentLikePath = () => `/comment/likes`;
const postCommentLike = async ({ commentId, likes }: RequestCommentLike) => {
  const response = await fetchInstance.post(
    postCommentLikePath(),
    {
      commentId,
      likes,
    },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostCommentLike = () => {
  return useMutation({
    mutationFn: ({ commentId, likes }: RequestCommentLike) => postCommentLike({ commentId, likes }),
  });
};
