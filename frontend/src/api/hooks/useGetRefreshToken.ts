import { useSuspenseQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';

export const getRefreshTokenPath = () => '/refresh-token';

export const getRefreshToken = async () => {
  await fetchInstance.get(getRefreshTokenPath(), { withCredentials: true });
  return null;
};

export const useGetRefreshToken = () => {
  return useSuspenseQuery({
    queryKey: ['refreshToken'],
    queryFn: () => getRefreshToken(),
  });
};
