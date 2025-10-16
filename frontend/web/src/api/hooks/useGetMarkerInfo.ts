import { useQuery } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/src/api/instance';
import { MarkerInfo } from '@/types';

export const getMarkerInfoPath = (id: string) => `/places/${id}/marker`;
export const getMarkerInfo = async (id: string) => {
  const response = await fetchInstance.get<MarkerInfo>(getMarkerInfoPath(id));
  return response.data;
};
export const useGetMarkerInfo = (id: string, enabled: boolean) => {
  return useQuery({
    queryKey: ['markerInfo', id],
    queryFn: () => getMarkerInfo(id),
    enabled,
    staleTime: 1000 * 60 * 5,
  });
};
