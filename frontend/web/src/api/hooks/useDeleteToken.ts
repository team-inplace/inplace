import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '../instance';

export const deleteTokenPath = () => '/refresh-token';

export const deleteRefreshToken = async () => {
  await fetchInstance.delete(deleteTokenPath(), { withCredentials: true });
  return null;
};

export const useDeleteToken = () => {
  return useMutation({
    mutationFn: () => deleteRefreshToken(),
  });
};
