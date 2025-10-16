import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';

export const deleteFCMTokenPath = () => '/alarms';

export const deleteFCMToken = async () => {
  await fetchInstance.delete(deleteFCMTokenPath(), { withCredentials: true });
  return null;
};

export const useDeleteFCMToken = () => {
  return useMutation({
    mutationFn: () => deleteFCMToken(),
  });
};
