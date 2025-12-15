import { useMutation, useQueryClient } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { RequestInfluencerLike, InfluencerData, PageableData } from '@/types';

interface PaginatedData {
  content: InfluencerData[];
  [key: string]: unknown;
}
interface InfiniteQueryData {
  pages: PageableData<InfluencerData>[];
  pageParams: number[];
}

interface InfiniteDataStructure {
  pages: (PaginatedData | InfluencerData[])[];
  pageParams: unknown[];
}

export const postInfluencerLikePath = () => `/influencers/likes`;
const postInfluencerLike = async ({ influencerId, likes }: RequestInfluencerLike) => {
  const response = await getFetchInstance().post(
    postInfluencerLikePath(),
    { influencerId, likes },
    { withCredentials: true },
  );
  return response.data;
};

const isPaginated = (data: unknown): data is PaginatedData => {
  return (
    !!data && typeof data === 'object' && 'content' in data && Array.isArray((data as { content: unknown }).content)
  );
};

const isInfinite = (data: unknown): data is InfiniteDataStructure => {
  return !!data && typeof data === 'object' && 'pages' in data && Array.isArray((data as { pages: unknown }).pages);
};

const updateInfluencerList = (oldData: unknown, targetId: number, likes: boolean): unknown => {
  if (!oldData) return oldData;

  if (Array.isArray(oldData)) {
    return (oldData as InfluencerData[]).map((item) => (item.influencerId === targetId ? { ...item, likes } : item));
  }

  if (isPaginated(oldData)) {
    return {
      ...oldData,
      content: oldData.content.map((item) => (item.influencerId === targetId ? { ...item, likes } : item)),
    };
  }

  if (isInfinite(oldData)) {
    return {
      ...oldData,
      pages: oldData.pages.map((page) => {
        if (Array.isArray(page)) {
          return (page as InfluencerData[]).map((item) => (item.influencerId === targetId ? { ...item, likes } : item));
        }
        if (isPaginated(page)) {
          return {
            ...page,
            content: page.content.map((item) => (item.influencerId === targetId ? { ...item, likes } : item)),
          };
        }
        return page;
      }),
    };
  }

  return oldData;
};

export const usePostInfluencerLike = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: postInfluencerLike,

    onMutate: async ({ influencerId, likes }) => {
      await Promise.all([
        queryClient.cancelQueries({ queryKey: ['influencers'] }),
        queryClient.cancelQueries({ queryKey: ['UserInfluencer'] }),
        queryClient.cancelQueries({ queryKey: ['myInfluencerVideo'] }),
      ]);
      const prevInfluencers = queryClient.getQueryData<InfluencerData[]>(['influencers']);
      const prevUserInfluencers = queryClient.getQueriesData({ queryKey: ['UserInfluencer'] });
      const prevMyInfluencers = queryClient.getQueryData<InfluencerData[]>(['myInfluencerVideo']);

      queryClient.setQueryData(['influencers'], (old: InfluencerData[] | undefined) =>
        updateInfluencerList(old, influencerId, likes),
      );

      queryClient.setQueriesData({ queryKey: ['UserInfluencer'] }, (old: InfiniteQueryData) => {
        return updateInfluencerList(old, influencerId, likes);
      });

      queryClient.setQueryData(['myInfluencerVideo'], (old: InfluencerData[] | undefined) =>
        updateInfluencerList(old, influencerId, likes),
      );

      return { prevInfluencers, prevUserInfluencers, prevMyInfluencers };
    },

    onError: (_err, _vars, context) => {
      if (context) {
        const { prevInfluencers, prevUserInfluencers, prevMyInfluencers } = context;
        queryClient.setQueryData(['influencers'], prevInfluencers);
        queryClient.setQueryData(['UserInfluencer'], prevUserInfluencers);
        queryClient.setQueryData(['myInfluencerVideo'], prevMyInfluencers);
      }
      alert('좋아요 등록에 실패했어요. 다시 시도해주세요!');
    },

    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['influencers'] });
      queryClient.invalidateQueries({ queryKey: ['myInfluencerVideo'] });
    },
  });
};
