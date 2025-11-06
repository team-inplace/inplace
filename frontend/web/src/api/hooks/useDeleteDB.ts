import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const deleteDBPath = () => '/temp';

export const deleteDB = async () => {
  await getFetchInstance().delete(deleteDBPath(), { withCredentials: true });
  return null;
};

export const useDeleteDB = () => {
  return useMutation({
    mutationFn: () => deleteDB(),
  });
};
