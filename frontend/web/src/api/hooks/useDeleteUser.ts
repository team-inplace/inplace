import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '../instance';

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
