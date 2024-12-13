import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '../instance';

export const deleteDBPath = () => '/temp';

export const deleteDB = async () => {
  await fetchInstance.delete(deleteDBPath(), { withCredentials: true });
  return null;
};

export const useDeleteDB = () => {
  return useMutation({
    mutationFn: () => deleteDB(),
  });
};
