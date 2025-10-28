import { useMutation } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { RequestInfluencerLike } from '@/types';

export const postInfluencerLikePath = () => `/influencers/likes`;
const postInfluencerLike = async ({ influencerId, likes }: RequestInfluencerLike) => {
  const response = await getFetchInstance().post(
    postInfluencerLikePath(),
    {
      influencerId,
      likes,
    },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostInfluencerLike = () => {
  return useMutation({
    mutationFn: ({ influencerId, likes }: RequestInfluencerLike) => postInfluencerLike({ influencerId, likes }),
  });
};
