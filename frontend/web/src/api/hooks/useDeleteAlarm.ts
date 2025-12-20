import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const deleteAlarmPath = (id: string) => `/alarms/${id}`;

export const deleteAlarm = async (id: string) => {
  const response = await getFetchInstance().delete(deleteAlarmPath(id), { withCredentials: true });
  return response.data;
};

export const useDeleteAlarm = () => {
  return useMutation({
    mutationFn: (id: string) => deleteAlarm(id),
  });
};
