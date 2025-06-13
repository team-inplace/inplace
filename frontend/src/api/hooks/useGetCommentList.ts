import { useQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { PageableData, CommentData } from '@/types';

export const getCommentList = async (id: string, page: number, size: number) => {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString(),
  });
  const response = await fetchInstance.get<PageableData<CommentData>>(`/posts/${id}/comments?${params}`);
  return response.data;
};
export const useGetCommentList = (id: string, page: number, size: number) => {
  return useQuery({
    queryKey: ['commentList', page, size, id],
    queryFn: () => getCommentList(id, page, size),
  });
};
