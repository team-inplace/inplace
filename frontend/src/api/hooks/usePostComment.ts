import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { PostCommentProps } from '@/types';

export const postCommentPath = (boardId: string) => `/board/${boardId}/comment`;
const postComment = async ({ boardId, comment }: PostCommentProps) => {
  const response = await fetchInstance.post(
    postCommentPath(boardId),
    {
      comment,
    },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostComment = () => {
  return useMutation({
    mutationFn: ({ boardId, comment }: PostCommentProps) => postComment({ boardId, comment }),
  });
};
