import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '../instance';

export const deleteAlarmPath = (id: string) => `/alarms/${id}`;

export const deleteAlarm = async (id: string) => {
  const response = await fetchInstance.delete(deleteAlarmPath(id), { withCredentials: true });
  return response.data;
};

export const useDeleteAlarm = () => {
  return useMutation({
    mutationFn: (id: string) => deleteAlarm(id),
  });
};
