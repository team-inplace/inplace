import { useQuery } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { CommentPositionData } from '@/types';

export const getCommentPosition = async (postId: string | number, commentId: string | number) => {
  const response = await getFetchInstance().get<CommentPositionData>(
    `/posts/${postId}/comments/${commentId}/position`,
    { withCredentials: true },
  );
  return response.data;
};

export const useGetCommentPage = (postId: string | number, commentId: string | number, enabled = true) => {
  return useQuery({
    queryKey: ['commentPage', postId, commentId],
    queryFn: () => getCommentPosition(postId, commentId),
    enabled,
  });
};
