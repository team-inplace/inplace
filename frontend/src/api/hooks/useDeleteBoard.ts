import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '../instance';

export const deleteBoardPath = (id: string) => `/board/${id}`;
const deleteBoard = async (id: string) => {
  const response = await fetchInstance.delete(deleteBoardPath(id), { withCredentials: true });
  return response.data;
};

export const useDeleteBoard = () => {
  return useMutation({
    mutationFn: (id: string) => deleteBoard(id),
  });
};
