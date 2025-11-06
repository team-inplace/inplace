import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { PostCommentProps } from '@/types';

export const postCommentPath = (postId: string) => `/posts/${postId}/comments`;
const postComment = async ({ postId, comment }: PostCommentProps) => {
  const response = await getFetchInstance().post(
    postCommentPath(postId),
    {
      comment,
    },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostComment = () => {
  return useMutation({
    mutationFn: ({ postId, comment }: PostCommentProps) => postComment({ postId, comment }),
  });
};
