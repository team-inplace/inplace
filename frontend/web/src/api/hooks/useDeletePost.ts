import { useMutation } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const deletePostPath = (id: string) => `/posts/${id}`;
const deletePost = async (id: string) => {
  const response = await getFetchInstance().delete(deletePostPath(id), { withCredentials: true });
  return response.data;
};

export const useDeletePost = () => {
  return useMutation({
    mutationFn: (id: string) => deletePost(id),
  });
};
