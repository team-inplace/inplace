import { useQuery } from '@tanstack/react-query';

import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';
import { SearchComplete } from '@/types';

export const getSearchCompletePath = () => `/search/complete`;
const getSearchComplete = async (value: string, type: string) => {
  const params = new URLSearchParams({
    value: value.toString(),
    type: type.toString(),
  });
  const response = await fetchInstance.get<SearchComplete[]>(`${getSearchCompletePath()}?${params}`);
  return response.data;
};

export const useGetSearchComplete = (value: string, type: string, enabled: boolean) => {
  return useQuery({
    queryKey: ['searchComplete', value, type],
    queryFn: () => getSearchComplete(value, type),
    staleTime: 1000 * 60 * 5,
    enabled,
  });
};
