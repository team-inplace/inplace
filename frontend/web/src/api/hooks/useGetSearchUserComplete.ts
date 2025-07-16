import { useQuery } from '@tanstack/react-query';

import { fetchInstance } from '../instance';
import { SearchUserComplete } from '@/types';

export const getSearchUserCompletePath = (postId: string) => `/posts/${postId}/comments/complete`;
const getSearchUserComplete = async (postId: string, value: string) => {
  const params = new URLSearchParams({
    value: value.toString(),
  });
  const response = await fetchInstance.get<SearchUserComplete[]>(`${getSearchUserCompletePath(postId)}?${params}`);
  return response.data;
};

export const useGetSearchUserComplete = (postId: string, value: string, enabled: boolean) => {
  return useQuery({
    queryKey: ['searchUserComplete', postId, value],
    queryFn: () => getSearchUserComplete(postId, value),
    staleTime: 1000 * 60 * 5,
    enabled,
  });
};
