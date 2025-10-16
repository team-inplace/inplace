import { useSuspenseQuery } from '@tanstack/react-query';
import { PostData } from '@/types';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';

export const getPostDataPath = (id: string) => `/posts/${id}`;
export const getPostData = async (id: string) => {
  const response = await fetchInstance.get<PostData>(getPostDataPath(id), { withCredentials: true });
  return response.data;
};
export const useGetPostData = (id: string) => {
  return useSuspenseQuery({
    queryKey: ['postData', id],
    queryFn: () => getPostData(id),
    staleTime: 1000 * 60 * 5,
  });
};
