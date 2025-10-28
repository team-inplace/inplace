import { useSuspenseQuery } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

interface Influencer {
  influencerName: string;
}

const useGetDropdownInfluencer = () => {
  return useSuspenseQuery({
    queryKey: ['influencersName'],
    queryFn: async () => {
      const { data } = await getFetchInstance().get<Influencer[]>('/influencers/names');
      return data.map((influencer) => ({
        label: influencer.influencerName,
      }));
    },
    staleTime: 1000 * 60 * 5,
  });
};

export default useGetDropdownInfluencer;
