import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';
import { RequestCommentLike } from '@/types';

export const postCommentLikePath = (postId: string) => `/posts/${postId}/comments/likes`;
const postCommentLike = async ({ postId, commentId, likes }: RequestCommentLike) => {
  const response = await fetchInstance.post(
    postCommentLikePath(postId),
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
    mutationFn: ({ postId, commentId, likes }: RequestCommentLike) => postCommentLike({ postId, commentId, likes }),
  });
};
