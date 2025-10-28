import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

import { MultipleLikeRequest } from '@/types';

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
  return useMutation({
    mutationFn: ({ influencerIds, likes }: MultipleLikeRequest) => postMultipleInfluencerLike({ influencerIds, likes }),
  });
};
