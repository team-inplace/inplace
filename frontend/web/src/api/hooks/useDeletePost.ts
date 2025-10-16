import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';

export const deletePostPath = (id: string) => `/posts/${id}`;
const deletePost = async (id: string) => {
  const response = await fetchInstance.delete(deletePostPath(id), { withCredentials: true });
  return response.data;
};

export const useDeletePost = () => {
  return useMutation({
    mutationFn: (id: string) => deletePost(id),
  });
};
