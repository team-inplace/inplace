import { useQuery } from '@tanstack/react-query';
import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const getSendInfoPath = (id: string) => `/place-message/${id}`;
export const getSendInfo = async (id: string) => {
  const response = await getFetchInstance().get(getSendInfoPath(id), { withCredentials: true });
  return response.data;
};
export const useGetSendInfo = (id: string, enabled: boolean) => {
  return useQuery({ queryKey: ['sendInfo', id], queryFn: () => getSendInfo(id), enabled });
};
