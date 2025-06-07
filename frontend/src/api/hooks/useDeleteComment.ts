import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '../instance';

export const deleteCommentPath = (id: string) => `/comment/${id}`;
const deleteComment = async (id: string) => {
  const response = await fetchInstance.delete(deleteCommentPath(id), { withCredentials: true });
  return response.data;
};

export const useDeleteComment = () => {
  return useMutation({
    mutationFn: (id: string) => deleteComment(id),
  });
};
