import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { PostData, RequestPostLike } from '@/types';

export const postPostLikePath = () => `/posts/likes`;
const postPostLike = async ({ postId, likes }: RequestPostLike) => {
  const response = await getFetchInstance().post(
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
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: postPostLike,

    onMutate: async ({ postId, likes }) => {
      await queryClient.cancelQueries({ queryKey: ['postData', String(postId)] });

      const previousPost = queryClient.getQueryData<PostData>(['postData', String(postId)]);

      queryClient.setQueryData<PostData>(['postData', String(postId)], (old) => {
        if (!old) return old;
        return {
          ...old,
          selfLike: likes,
          totalLikeCount: likes ? old.totalLikeCount + 1 : Math.max(0, old.totalLikeCount - 1),
        };
      });

      return { previousPost };
    },

    onError: (_err, { postId }, context) => {
      if (context) {
        queryClient.setQueryData(['postData', String(postId)], context.previousPost);
      }
      alert('좋아요 처리에 실패했습니다.');
    },

    onSettled: (_data, _err, { postId }) => {
      queryClient.invalidateQueries({ queryKey: ['postData', String(postId)] });
    },
  });
};
