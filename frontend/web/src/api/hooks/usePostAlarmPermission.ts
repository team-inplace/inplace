import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const postAlarmPermissionPath = () => `/alarms/permission`;

const postAlarmPermission = async (permission: boolean) => {
  const response = await getFetchInstance().post(postAlarmPermissionPath(), { permission }, { withCredentials: true });
  return response.data;
};

export const usePostAlarmPermission = () => {
  return useMutation({
    mutationFn: (permission: boolean) => postAlarmPermission(permission),
  });
};
