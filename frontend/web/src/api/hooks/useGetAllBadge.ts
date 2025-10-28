import { useQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { BadgeData } from '@/types';

export const getAllBadgePath = () => `/users/all-badges`;

export const getAllBadge = async () => {
  const response = await fetchInstance.get<BadgeData[]>(getAllBadgePath(), { withCredentials: true });
  return response.data;
};
export const useGetAllBadge = () => {
  return useQuery({
    queryKey: ['allBadge'],
    queryFn: getAllBadge,
  });
};
