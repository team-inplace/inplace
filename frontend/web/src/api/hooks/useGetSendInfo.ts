import { useQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';

export const getSendInfoPath = (id: string) => `/place-message/${id}`;
export const getSendInfo = async (id: string) => {
  const response = await fetchInstance.get(getSendInfoPath(id), { withCredentials: true });
  return response.data;
};
export const useGetSendInfo = (id: string, enabled: boolean) => {
  return useQuery({ queryKey: ['sendInfo', id], queryFn: () => getSendInfo(id), enabled });
};
