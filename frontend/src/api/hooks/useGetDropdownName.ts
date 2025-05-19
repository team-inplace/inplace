import { useSuspenseQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';

interface Influencer {
  influencerName: string;
}

const useGetDropdownInfluencer = () => {
  return useSuspenseQuery({
    queryKey: ['influencersName'],
    queryFn: async () => {
      const { data } = await fetchInstance.get<Influencer[]>('/influencers/names');
      return data.map((influencer) => ({
        label: influencer.influencerName,
      }));
    },
    staleTime: 1000 * 60 * 5,
  });
};

export default useGetDropdownInfluencer;
