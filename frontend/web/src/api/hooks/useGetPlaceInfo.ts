import { useSuspenseQuery } from '@tanstack/react-query';
import { PlaceInfo } from '@/types';
import { fetchInstance } from '../instance';

export const getPlaceInfoPath = (id: string) => `/places/${id}`;
export const getPlaceInfo = async (id: string) => {
  const response = await fetchInstance.get<PlaceInfo>(getPlaceInfoPath(id), {
    withCredentials: true,
  });
  return response.data;
};
export const useGetPlaceInfo = (id: string) => {
  return useSuspenseQuery({
    queryKey: ['placeInfo', id],
    queryFn: () => getPlaceInfo(id),
    staleTime: 1000 * 60 * 5,
  });
};
