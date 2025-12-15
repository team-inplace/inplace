import { useMutation, useQueryClient } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { PageableData, CommentData, RequestCommentLike } from '@/types';

export const postCommentLikePath = (postId: string) => `/posts/${postId}/comments/likes`;

const postCommentLike = async ({ postId, commentId, likes }: RequestCommentLike) => {
  const response = await getFetchInstance().post(
    postCommentLikePath(postId),
    { commentId, likes },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostCommentLike = (postId: string) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: postCommentLike,

    onMutate: async ({ commentId, likes }) => {
      await queryClient.cancelQueries({ queryKey: ['commentList', postId] });

      const previousComments = queryClient.getQueriesData({ queryKey: ['commentList', postId] });

      queryClient.setQueriesData<PageableData<CommentData>>({ queryKey: ['commentList', postId] }, (oldData) => {
        if (!oldData || !oldData.content) return oldData;

        return {
          ...oldData,
          content: oldData.content.map((comment) => {
            if (comment.commentId === commentId) {
              return {
                ...comment,
                likes,
              };
            }
            return comment;
          }),
        };
      });

      return { previousComments };
    },

    onError: (_err, _vars, context) => {
      if (context?.previousComments) {
        context.previousComments.forEach(([queryKey, data]) => {
          queryClient.setQueryData(queryKey, data);
        });
      }
      alert('좋아요 처리에 실패했습니다.');
    },

    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['commentList', postId] });
    },
  });
};
