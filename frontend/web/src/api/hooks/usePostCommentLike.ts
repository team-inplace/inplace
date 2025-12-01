import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { CommentData, RequestCommentLike } from '@/types';
import useOptimisticUpdate from '@/hooks/useOptimisticUpdate';

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

export const usePostCommentLike = (postId: string) => {
  return useOptimisticUpdate<any, RequestCommentLike>({
    mutationFn: postCommentLike,
    queryKey: ['commentList', postId],

    updater: (oldData: CommentData[] | undefined, variables) => {
      if (!oldData) return [];
      return oldData.map((comment) =>
        comment.commentId === variables.commentId ? { ...comment, likes: variables.likes } : comment,
      );
    },
  });
};
