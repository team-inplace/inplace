import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '../instance';

export const postAlarmPermissionPath = () => `/alarms/permission`;

const postAlarmPermission = async (permission: boolean) => {
  const response = await fetchInstance.post(postAlarmPermissionPath(), { permission }, { withCredentials: true });
  return response.data;
};

export const usePostAlarmPermission = () => {
  return useMutation({
    mutationFn: (permission: boolean) => postAlarmPermission(permission),
  });
};
