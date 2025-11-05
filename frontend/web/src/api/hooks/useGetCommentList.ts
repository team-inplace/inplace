import { useQuery } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { PageableData, CommentData } from '@/types';

export const getCommentList = async (id: string, page: number, size: number) => {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
  });
  const response = await getFetchInstance().get<PageableData<CommentData>>(`/posts/${id}/comments?${params}`, {
    withCredentials: true,
  });
  return response.data;
};
export const useGetCommentList = (id: string, page: number, size: number) => {
  return useQuery({
    queryKey: ['commentList', id, page, size],
    queryFn: () => getCommentList(id, page, size),
  });
};
