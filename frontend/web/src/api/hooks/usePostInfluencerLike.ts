import { useMutation, useQueryClient } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { RequestInfluencerLike, InfluencerData } from '@/types';

export const postInfluencerLikePath = () => `/influencers/likes`;
const postInfluencerLike = async ({ influencerId, likes }: RequestInfluencerLike) => {
  const response = await getFetchInstance().post(
    postInfluencerLikePath(),
    { influencerId, likes },
    { withCredentials: true },
  );
  return response.data;
};

const updateInfluencerList = (
  list: InfluencerData[] | undefined,
  targetId: number,
  likes: boolean,
): InfluencerData[] => {
  if (!list) return [];
  return list.map((item) => (item.influencerId === targetId ? { ...item, likes } : item));
};

interface SnapShotContext {
  prevUserInfluencers: InfluencerData[] | undefined;
  prevMyInfluencers: InfluencerData[] | undefined;
}

export const usePostInfluencerLike = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: postInfluencerLike,

    onMutate: async ({ influencerId, likes }) => {
      await Promise.all([
        queryClient.cancelQueries({ queryKey: ['UserInfluencer'] }),
        queryClient.cancelQueries({ queryKey: ['myInfluencerVideo'] }),
      ]);

      const prevUserInfluencers = queryClient.getQueryData<InfluencerData[]>(['UserInfluencer']);
      const prevMyInfluencers = queryClient.getQueryData<InfluencerData[]>(['myInfluencerVideo']);

      queryClient.setQueryData(['UserInfluencer'], (old: InfluencerData[] | undefined) =>
        updateInfluencerList(old, influencerId, likes),
      );

      queryClient.setQueryData(['myInfluencerVideo'], (old: InfluencerData[] | undefined) =>
        updateInfluencerList(old, influencerId, likes),
      );

      return { prevUserInfluencers, prevMyInfluencers };
    },

    onError: (_err, _vars, context) => {
      if (context) {
        const { prevUserInfluencers, prevMyInfluencers } = context as SnapShotContext;
        queryClient.setQueryData(['UserInfluencer'], prevUserInfluencers);
        queryClient.setQueryData(['myInfluencerVideo'], prevMyInfluencers);
      }
      alert('좋아요 등록에 실패했어요. 다시 시도해주세요!');
    },

    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['UserInfluencer'] });
      queryClient.invalidateQueries({ queryKey: ['myInfluencerVideo'] });
    },
  });
};
