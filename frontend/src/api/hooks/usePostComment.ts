import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { PostCommentProps } from '@/types';

export const postCommentPath = (postId: string) => `/posts/${postId}/comments`;
const postComment = async ({ postId, comment }: PostCommentProps) => {
  const response = await fetchInstance.post(
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
