import { useMutation } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { RequestCommentLike } from '@/types';

export const postCommentLikePath = (postId: string) => `/posts/${postId}/comments/likes`;
const postCommentLike = async ({ postId, commentId, likes }: RequestCommentLike) => {
  const response = await getFetchInstance().post(
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
