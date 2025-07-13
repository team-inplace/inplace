import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '../instance';

import { MultipleLikeRequest } from '@/types';

export const postMultipleInfluencerLike = async ({ influencerIds, likes }: MultipleLikeRequest) => {
  const response = await fetchInstance.post(
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
