import { useQuery } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';
import { AlarmData } from '@/types';

export const getAlarmsPath = () => `/alarms`;

export const getAlarms = async () => {
  const response = await getFetchInstance().get<AlarmData[]>(getAlarmsPath(), { withCredentials: true });
  return response.data;
};
export const useGetAlarms = (enabled: boolean) => {
  return useQuery({
    queryKey: ['alarms'],
    queryFn: getAlarms,
    enabled,
  });
};
