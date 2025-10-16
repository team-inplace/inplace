import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';

export const postDeviceTokenPath = () => `/alarms`;
const postDeviceToken = async (token: string) => {
  const response = await fetchInstance.post(postDeviceTokenPath(), { token }, { withCredentials: true });
  return response.data;
};

export const usePostDeviceToken = () => {
  return useMutation({
    mutationFn: (token: string) => postDeviceToken(token),
  });
};
