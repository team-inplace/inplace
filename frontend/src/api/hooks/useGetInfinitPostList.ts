import { useInfiniteQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { PageableData, PostListData } from '@/types';

export const getInfinitPostList = async (
  page: number,
  size: number,
  sort: string,
): Promise<PageableData<PostListData>> => {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
    sort,
  });

  const response = await fetchInstance.get<PageableData<PostListData>>(`/posts?${params}`, { withCredentials: true });
  return response.data;
};

interface QueryParams {
  size: number;
  sort: string;
}

export const useGetInfinitPostList = ({ size, sort }: QueryParams, enabled?: boolean) => {
  return useInfiniteQuery<
    PageableData<PostListData>,
    Error,
    { pages: PageableData<PostListData>[]; pageParams: number[] },
    [string, number, string],
    number
  >({
    queryKey: ['infinitePostList', size, sort],
    queryFn: ({ pageParam = 0 }) => getInfinitPostList(pageParam, size, sort),
    initialPageParam: 0,
    getNextPageParam: (lastPage) => {
      return lastPage.last ? undefined : lastPage.number + 1;
    },
    enabled,
    staleTime: 1000 * 60 * 5,
  });
};
