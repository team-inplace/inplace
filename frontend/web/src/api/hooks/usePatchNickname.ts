import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '@inplace-frontend-monorepo/shared/api/instance';

export const patchNicknamePath = () => `/users/nickname`;
const patchNickname = async (nickname: string) => {
  const response = await fetchInstance.patch(`${patchNicknamePath()}`, { nickname }, { withCredentials: true });
  return response.data;
};

export const usePatchNickname = () => {
  return useMutation({
    mutationFn: (nickname: string) => patchNickname(nickname),
  });
};
