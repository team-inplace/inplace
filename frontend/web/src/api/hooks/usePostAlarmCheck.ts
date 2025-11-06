import { useMutation } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

type PostAlarmCheckProps = {
  alarmId: number;
};

export const postAlarmCheckPath = ({ alarmId }: PostAlarmCheckProps) => `/alarms/${alarmId}`;
const postAlarmCheck = async ({ alarmId }: PostAlarmCheckProps) => {
  const response = await getFetchInstance().post(postAlarmCheckPath({ alarmId }), {}, { withCredentials: true });
  return response.data;
};

export const usePostAlarmCheck = () => {
  return useMutation({
    mutationFn: ({ alarmId }: PostAlarmCheckProps) => postAlarmCheck({ alarmId }),
  });
};
