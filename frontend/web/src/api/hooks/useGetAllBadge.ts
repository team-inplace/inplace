import { useQuery } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { BadgeData } from '@/types';

export const getAllBadgePath = () => `/users/all-badges`;

export const getAllBadge = async () => {
  const response = await getFetchInstance().get<BadgeData[]>(getAllBadgePath(), { withCredentials: true });
  return response.data;
};
export const useGetAllBadge = () => {
  return useQuery({
    queryKey: ['allBadge'],
    queryFn: getAllBadge,
  });
};
