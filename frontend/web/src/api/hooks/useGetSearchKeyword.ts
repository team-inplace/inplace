import { useQuery } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const getSearchKeywordPath = () => `/search/keyword`;

export const getSearchKeyword = async (value: string, x: number, y: number) => {
  const params = new URLSearchParams({
    value: value.toString(),
    x: x.toString(),
    y: y.toString(),
  });

  const response = await getFetchInstance().get(`${getSearchKeywordPath()}?${params}`, {
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
