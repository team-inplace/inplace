import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { PostData, RequestPostLike } from '@/types';
import useOptimisticUpdate from '@/hooks/useOptimisticUpdate';

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
  return useOptimisticUpdate<PostData[], RequestPostLike>({
    mutationFn: postPostLike,
    queryKey: ['postData'],

    updater: (oldData, variables) => {
      if (!oldData) return [];
      return oldData.map((post) => (post.postId === variables.postId ? { ...post, likes: variables.likes } : post));
    },
  });
};
