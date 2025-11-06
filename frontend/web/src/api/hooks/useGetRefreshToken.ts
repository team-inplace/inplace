import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const getRefreshTokenPath = () => '/refresh-token';

export const getRefreshToken = async () => {
  await getFetchInstance().get(getRefreshTokenPath(), { withCredentials: true });
  return null;
};

export const useGetRefreshToken = () => {
  return useMutation({
    mutationFn: () => getRefreshToken(),
  });
};
