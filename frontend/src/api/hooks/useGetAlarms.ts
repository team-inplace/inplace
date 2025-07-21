import { useQuery } from '@tanstack/react-query';

import { fetchInstance } from '../instance';
import { AlarmData } from '@/types';

export const getAlarmsPath = () => `/alarms`;

export const getAlarms = async () => {
  const response = await fetchInstance.get<AlarmData[]>(getAlarmsPath(), { withCredentials: true });
  return response.data;
};
export const useGetAlarms = (enabled: boolean) => {
  return useQuery({
    queryKey: ['alarms'],
    queryFn: getAlarms,
    enabled,
  });
};
