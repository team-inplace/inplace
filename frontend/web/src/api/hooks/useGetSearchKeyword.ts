import { useQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';

export const getSearchKeywordPath = () => `/search/keyword`;

export const getSearchKeyword = async (value: string, x: number, y: number) => {
  const params = new URLSearchParams({
    value: value.toString(),
    x: x.toString(),
    y: y.toString(),
  });

  const response = await fetchInstance.get(`${getSearchKeywordPath()}?${params}`, {
    withCredentials: true,
  });
  return response.data;
};
export const useGetSearchKeyword = (value: string, x: number, y: number, enabled: boolean) => {
  return useQuery({
    queryKey: ['searchKeyword', value, x, y],
    queryFn: () => getSearchKeyword(value, x, y),
    enabled,
  });
};
