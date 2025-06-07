import { useInfiniteQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { PageableData, BoardListData } from '@/types';

export const getInfinitBoardList = async (
  page: number,
  size: number,
  sort: string,
): Promise<PageableData<BoardListData>> => {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
    sort,
  });

  const response = await fetchInstance.get<PageableData<BoardListData>>(`/board?${params}`, { withCredentials: true });
  return response.data;
};

interface QueryParams {
  size: number;
  sort: string;
}

export const useGetInfinitBoardList = ({ size, sort }: QueryParams, enabled?: boolean) => {
  return useInfiniteQuery<
    PageableData<BoardListData>,
    Error,
    { pages: PageableData<BoardListData>[]; pageParams: number[] },
    [string, number, string],
    number
  >({
    queryKey: ['infiniteBoardList', size, sort],
    queryFn: ({ pageParam = 0 }) => getInfinitBoardList(pageParam, size, sort),
    initialPageParam: 0,
    getNextPageParam: (lastPage) => {
      return lastPage.last ? undefined : lastPage.number + 1;
    },
    enabled,
    staleTime: 1000 * 60 * 5,
  });
};
