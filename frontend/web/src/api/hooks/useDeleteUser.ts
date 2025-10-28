import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const deleteUserPath = () => '/users';

export const deleteUser = async () => {
  await getFetchInstance().delete(deleteUserPath(), { withCredentials: true });
  return null;
};

export const useDeleteUser = () => {
  return useMutation({
    mutationFn: () => deleteUser(),
  });
};
