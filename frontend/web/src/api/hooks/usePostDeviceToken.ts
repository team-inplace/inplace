import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const postDeviceTokenPath = () => `/alarms`;
const postDeviceToken = async (token: string) => {
  const response = await getFetchInstance().post(postDeviceTokenPath(), { token }, { withCredentials: true });
  return response.data;
};

export const usePostDeviceToken = () => {
  return useMutation({
    mutationFn: (token: string) => postDeviceToken(token),
  });
};
