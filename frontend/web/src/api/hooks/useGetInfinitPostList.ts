import { useInfiniteQuery } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { CursorData, PostListData } from '@/types';

export const getInfinitPostList = async (
  cursorId: number | null,
  cursorValue: number | null,
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

  if (cursorValue !== null) {
    params.append('cursorValue', cursorValue.toString());
  }

  const response = await getFetchInstance().get<CursorData<PostListData>>(`/posts?${params}`, {
    withCredentials: true,
  });
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
    { pages: CursorData<PostListData>[]; pageParams: ({ cursorId: number; cursorValue: number } | null)[] },
    [string, number, string],
    { cursorId: number; cursorValue: number } | null
  >({
    queryKey: ['infinitePostList', size, sort],
    queryFn: ({ pageParam = null }) =>
      getInfinitPostList(pageParam?.cursorId || null, pageParam?.cursorValue || null, size, sort),
    initialPageParam: null,
    getNextPageParam: (lastPage) => {
      return lastPage.cursor.hasNext
        ? { cursorId: lastPage.cursor.nextCursorId, cursorValue: lastPage.cursor.nextCursorValue }
        : undefined;
    },
    enabled,
    staleTime: 1000 * 60 * 5,
  });
};
