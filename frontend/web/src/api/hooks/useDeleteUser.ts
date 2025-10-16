import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';

export const deleteUserPath = () => '/users';

export const deleteUser = async () => {
  await fetchInstance.delete(deleteUserPath(), { withCredentials: true });
  return null;
};

export const useDeleteUser = () => {
  return useMutation({
    mutationFn: () => deleteUser(),
  });
};
