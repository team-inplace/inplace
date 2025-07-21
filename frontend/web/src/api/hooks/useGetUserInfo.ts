import { useQuery } from '@tanstack/react-query';
import { fetchInstance } from '../instance';
import { UserInfoData } from '@/types';

export const getUserInfoPath = () => `/users/info`;

export const getUserInfo = async () => {
  try {
    const response = await fetchInstance.get<UserInfoData>(getUserInfoPath(), {
      withCredentials: true,
    });
    return response.data;
  } catch (error) {
    console.error('Failed to fetch user info:', error);
    throw error;
  }
};

export const useGetUserInfo = () => {
  return useQuery<UserInfoData>({
    queryKey: ['UserInfo'],
    queryFn: () => getUserInfo(),
    staleTime: 2.9 * 60 * 1000,
    retry: false,
    throwOnError: false,
  });
};
