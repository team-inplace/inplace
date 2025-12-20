import { useMutation } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const patchNicknamePath = () => `/users/nickname`;
const patchNickname = async (nickname: string) => {
  const response = await getFetchInstance().patch(`${patchNicknamePath()}`, { nickname }, { withCredentials: true });
  return response.data;
};

export const usePatchNickname = () => {
  return useMutation({
    mutationFn: (nickname: string) => patchNickname(nickname),
  });
};
