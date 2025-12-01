import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

import { InfluencerData, MultipleLikeRequest } from '@/types';
import useOptimisticUpdate from '@/hooks/useOptimisticUpdate';

export const postMultipleInfluencerLike = async ({ influencerIds, likes }: MultipleLikeRequest) => {
  const response = await getFetchInstance().post(
    '/influencers/multiple/likes',
    {
      influencerIds,
      likes,
    },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostMultipleInfluencerLike = () => {
  return useOptimisticUpdate<any, MultipleLikeRequest>({
    mutationFn: postMultipleInfluencerLike,
    queryKey: ['UserInfluencer'],

    updater: (oldData: InfluencerData[] | undefined, variables) => {
      if (!oldData) return [];
      return oldData.map((item) =>
        variables.influencerIds.includes(item.influencerId) ? { ...item, likes: variables.likes } : item,
      );
    },
  });
};
