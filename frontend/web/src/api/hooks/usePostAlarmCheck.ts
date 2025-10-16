import { useMutation } from '@tanstack/react-query';
import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';

type PostAlarmCheckProps = {
  alarmId: number;
};

export const postAlarmCheckPath = ({ alarmId }: PostAlarmCheckProps) => `/alarms/${alarmId}`;
const postAlarmCheck = async ({ alarmId }: PostAlarmCheckProps) => {
  const response = await fetchInstance.post(postAlarmCheckPath({ alarmId }), {}, { withCredentials: true });
  return response.data;
};

export const usePostAlarmCheck = () => {
  return useMutation({
    mutationFn: ({ alarmId }: PostAlarmCheckProps) => postAlarmCheck({ alarmId }),
  });
};
