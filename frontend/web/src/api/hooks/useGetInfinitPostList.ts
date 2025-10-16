import { useInfiniteQuery } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';
import { CursorData, PostListData } from '@/types';

export const getInfinitPostList = async (
  cursorId: number | null,
  size: number,
  sort: string,
): Promise<CursorData<PostListData>> => {
  const params = new URLSearchParams({
    size: size.toString(),
    sort,
  });

  if (cursorId !== null) {
    params.append('cursorId', cursorId.toString());
  }

  const response = await fetchInstance.get<CursorData<PostListData>>(`/posts?${params}`, { withCredentials: true });
  return response.data;
};

interface QueryParams {
  size: number;
  sort: string;
}

export const useGetInfinitPostList = ({ size, sort }: QueryParams, enabled?: boolean) => {
  return useInfiniteQuery<
    CursorData<PostListData>,
    Error,
    { pages: CursorData<PostListData>[]; pageParams: (number | null)[] },
    [string, number, string],
    number | null
  >({
    queryKey: ['infinitePostList', size, sort],
    queryFn: ({ pageParam = null }) => getInfinitPostList(pageParam, size, sort),
    initialPageParam: null,
    getNextPageParam: (lastPage) => {
      return lastPage.cursor.hasNext ? lastPage.cursor.nextCursorId : undefined;
    },
    enabled,
    staleTime: 1000 * 60 * 5,
  });
};
