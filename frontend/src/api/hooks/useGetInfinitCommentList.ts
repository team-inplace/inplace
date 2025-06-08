import { useInfiniteQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { PageableData, CommentData } from '@/types';

export const getInfinitCommentList = async (
  page: number,
  size: number,
  id: string,
): Promise<PageableData<CommentData>> => {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
    id,
  });

  const response = await fetchInstance.get<PageableData<CommentData>>(`/posts/${id}/comments?${params}`, {
    withCredentials: true,
  });
  return response.data;
};

interface QueryParams {
  size: number;
  id: string;
}

export const useGetInfinitCommentList = ({ size, id }: QueryParams, enabled?: boolean) => {
  return useInfiniteQuery<
    PageableData<CommentData>,
    Error,
    { pages: PageableData<CommentData>[]; pageParams: number[] },
    [string, number, string],
    number
  >({
    queryKey: ['infiniteCommenList', size, id],
    queryFn: ({ pageParam = 0 }) => getInfinitCommentList(pageParam, size, id),
    initialPageParam: 0,
    getNextPageParam: (lastPage) => {
      return lastPage.last ? undefined : lastPage.number + 1;
    },
    enabled,
    staleTime: 1000 * 60 * 5,
  });
};
