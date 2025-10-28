import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const deleteTokenPath = () => '/refresh-token';

export const deleteRefreshToken = async () => {
  await getFetchInstance().delete(deleteTokenPath(), { withCredentials: true });
  return null;
};

export const useDeleteToken = () => {
  return useMutation({
    mutationFn: () => deleteRefreshToken(),
  });
};
