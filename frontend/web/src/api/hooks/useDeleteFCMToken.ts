import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const deleteFCMTokenPath = () => '/alarms';

export const deleteFCMToken = async () => {
  await getFetchInstance().delete(deleteFCMTokenPath(), { withCredentials: true });
  return null;
};

export const useDeleteFCMToken = () => {
  return useMutation({
    mutationFn: () => deleteFCMToken(),
  });
};
