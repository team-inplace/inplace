import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';

export const getRefreshTokenPath = () => '/refresh-token';

export const getRefreshToken = async () => {
  await fetchInstance.get(getRefreshTokenPath(), { withCredentials: true });
  return null;
};

export const useGetRefreshToken = () => {
  return useMutation({
    mutationFn: () => getRefreshToken(),
  });
};
