import { useQuery } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { SearchUserComplete } from '@/types';

export const getSearchUserCompletePath = (postId: string) => `/posts/${postId}/comments/complete`;
const getSearchUserComplete = async (postId: string, value: string) => {
  const params = new URLSearchParams({
    value: value.toString(),
  });
  const response = await getFetchInstance().get<SearchUserComplete[]>(
    `${getSearchUserCompletePath(postId)}?${params}`,
    {
      withCredentials: true,
    },
  );
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
