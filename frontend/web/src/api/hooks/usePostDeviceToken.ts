import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { AlarmTokenData } from '@/types';

export const postDeviceTokenPath = () => `/alarms`;
const postDeviceToken = async (token: AlarmTokenData) => {
  const response = await getFetchInstance().post(postDeviceTokenPath(), token, { withCredentials: true });
  return response.data;
};

export const usePostDeviceToken = () => {
  return useMutation({
    mutationFn: (token: AlarmTokenData) => postDeviceToken(token),
  });
};
